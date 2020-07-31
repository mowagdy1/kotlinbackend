package ktor

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import commons.*
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.util.pipeline.PipelineContext


fun Route.resolveRoutes() {
    ApplicationRoutes.routes.forEach { route ->
        route(route.uri) {

            when (route.method) {
                RouteMethod.GET -> get { handlingRequest(route, this) }
                RouteMethod.POST -> post { handlingRequest(route, this) }
                RouteMethod.PUT -> put { handlingRequest(route, this) }
                RouteMethod.DELETE -> delete { handlingRequest(route, this) }
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
            //route.handler.handle(ctx)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "auth is required")
        }
    }

    route.handler.handle(ctx)
}


class JsonMapperImpl : JsonMapper {
    private val mapper = jacksonObjectMapper()
    override fun <T> toJson(obj: T): String =
            mapper.writeValueAsString(obj)
}


abstract class BaseApiHandler<Request, Response> : BaseRouteHandler {
    override suspend fun handle(ctx: PipelineContext<Unit, ApplicationCall>) {
        val processor = specifyProcessor(ctx)
        val response = processor.execute()

        val jsonResponse = JsonMapperImpl().toJson(response)
        ctx.call.respondText(jsonResponse, ContentType.Application.Json, HttpStatusCode.OK)
    }

    suspend inline fun <reified Request : Any> readRequest(ctx: PipelineContext<Unit, ApplicationCall>): Request =
            ctx.context.receive()

    abstract suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>): BaseProcessor<Response>
}


private fun parseAuthorizationHeaderToToken(authHeader: String): String {
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ""
    }
    return authHeader.substring(7, authHeader.length)
}
