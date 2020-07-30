package ktor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import commons.AuthTokenManagerJWT
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.pipeline.PipelineContext
import modules.articles.ArticleListingProcessor
import modules.articles.ArticleRepoImpl
import modules.user.*

object ApplicationRoutes {
    val routes: MutableList<SingleRoute> = mutableListOf()

    fun registerRoute(endpoint: SingleRoute) {
        routes.add(endpoint)
    }
}


fun Route.applicationRoutes() {
    ApplicationRoutes.routes.forEach { route ->
        route(route.uri) {

            when (route.method) {
                HttpMethod.Get -> get { handlingRequest(route, this) }
                HttpMethod.Post -> post { handlingRequest(route, this) }
                HttpMethod.Put -> put { handlingRequest(route, this) }
            }

            get {
                call.respond(HttpStatusCode.BadRequest, "Wrong route method")
            }

        }
    }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.handlingRequest(route: SingleRoute, ctx: PipelineContext<Unit, ApplicationCall>) {

    if (route.auth) {
        val authHeader = call.request.header(HttpHeaders.Authorization)
        if (authHeader is String && authHeader.isNotEmpty()) {
            val parsedToken = AuthTokenManagerJWT().parseToken(parseAuthorizationHeaderToToken(authHeader))

            var eligibleEndpoint = false
            parsedToken.roles.forEach { role ->
                if (route.roles.contains(role)) {
                    eligibleEndpoint = true
                }
            }
            if (!eligibleEndpoint) {
                call.respond(HttpStatusCode.Forbidden, "Not have a permission")
            }

            //ctx.call.respondText("", ContentType.Application.Json, HttpStatusCode.OK)
            //call.respond(HttpStatusCode.OK, route.handler.handle(ctx))
            //route.handler.handle(ctx)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "auth is required")
        }
    }

    //call.respond(HttpStatusCode.OK, route.handler.execute()!!)
    route.handler.handle(ctx)
}


fun ApplicationRoutes.allRoutes() {

    registerRoute(SingleRoute(HttpMethod.Get, "p", ArticleListingHandler()))

    registerRoute(SingleRoute(HttpMethod.Get, "users", UserListingHandler()))
    registerRoute(SingleRoute(HttpMethod.Post, "users/register", UserRegisteringHandler()))

}

interface JsonMapper {
    fun <T> toJson(obj: T): String
}

class JsonMapperImpl : JsonMapper {
    private val mapper = jacksonObjectMapper()
    override fun <T> toJson(obj: T): String =
            mapper.writeValueAsString(obj)
}

interface BaseRouteHandler {
    suspend fun handle(ctx: PipelineContext<Unit, ApplicationCall>)
}

open class SingleRoute(val method: HttpMethod,
                       val uri: String,
                       val handler: BaseRouteHandler,
                       val auth: Boolean = false,
                       val roles: List<String> = listOf())

abstract class BaseApiHandler<Request, Response> : BaseRouteHandler {
    override suspend fun handle(ctx: PipelineContext<Unit, ApplicationCall>) {
        val processor = specifyProcessor(ctx)
        val response = processor.execute()

        val jsonResponse = JsonMapperImpl().toJson(response)
        ctx.call.respondText(jsonResponse, ContentType.Application.Json, HttpStatusCode.OK)
    }

    suspend inline fun <reified Request : Any> readRequest(ctx: PipelineContext<Unit, ApplicationCall>): Request =
            ctx.context.receive()
    //return ctx.call.receiveText()

    abstract suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>): BaseProcessor<Response>
}

abstract class BaseProcessor<Response> {
    abstract fun validate()
    abstract suspend fun process(): Response
    suspend fun execute(): Response {
        validate()
        return process()
    }
}


class ArticleListingHandler : BaseApiHandler<EmptyRequest, BanyanResponse>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            ArticleListingProcessor(ArticleRepoImpl())
}

class UserListingHandler : BaseApiHandler<EmptyRequest, List<UserListingResponse>>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            UserListingProcessor(UserRepoImpl())
}

class UserRegisteringHandler : BaseApiHandler<UserRegisterRequest, EmptyResponse>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            UserRegisteringProcessor(readRequest(ctx), UserRepoImpl())
}


data class BanyanRequest(val whatever: String = "")
data class BanyanResponse(val whatever: String = "")


private fun parseAuthorizationHeaderToToken(authHeader: String): String {
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ""
    }
    return authHeader.substring(7, authHeader.length)
}
