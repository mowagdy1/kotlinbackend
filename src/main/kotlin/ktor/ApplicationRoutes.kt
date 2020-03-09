package ktor

import commons.AuthTokenManagerJWT
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.pipeline.PipelineContext
import modules.user.EmptyRequest
import modules.user.UserListingHandler
import modules.user.UserRepoImpl

object ApplicationRoutes {
    val endpoints: MutableList<SingleEndpoint<*, *>> = mutableListOf()

    fun registerRoute(endpoint: SingleEndpoint<*, *>) {
        endpoints.add(endpoint)
    }

}


fun Route.applicationRoutes() {
    ApplicationRoutes.endpoints.forEach { endpoint ->
        route(endpoint.route) {

            when {
                endpoint.method == HttpMethod.Get -> get { handlingRequest(endpoint) }
                endpoint.method == HttpMethod.Post -> post { handlingRequest(endpoint) }
                endpoint.method == HttpMethod.Put -> put { handlingRequest(endpoint) }
            }

            get {
                call.respond(HttpStatusCode.BadRequest, "Wrong endpoint method")
            }

        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlingRequest(endpoint: SingleEndpoint<*, *>) {
    if (endpoint.auth) {
        val authHeader = call.request.header(HttpHeaders.Authorization)
        if (authHeader is String && authHeader.isNotEmpty()) {
            val parsedToken = AuthTokenManagerJWT().parseToken(parseAuthorizationHeaderToToken(authHeader))

            var eligibleEndpoint = false
            parsedToken.roles.forEach { role ->
                if (endpoint.roles.contains(role)) {
                    eligibleEndpoint = true
                }
            }
            if (!eligibleEndpoint) {
                call.respond(HttpStatusCode.Forbidden, "Not have a permission")
            }

            call.respond(HttpStatusCode.OK, endpoint.handler.execute()!!)
        } else {
            call.respond(HttpStatusCode.Unauthorized, "auth is required")
        }
    }
    call.respond(HttpStatusCode.OK, endpoint.handler.execute()!!)
}


suspend fun ApplicationRoutes.allRoutes() {

    registerRoute(SingleEndpoint(HttpMethod.Get, "p", ArticleListingHandler(BanyanRequest())))

    registerRoute(SingleEndpoint(HttpMethod.Get, "users", UserListingHandler(EmptyRequest(), UserRepoImpl())))

}


open class SingleEndpoint<RouteRequest, RouteResponse>(val method: HttpMethod,
                                                       val route: String,
                                                       val handler: BaseHandler<RouteRequest, RouteResponse>,
                                                       val auth: Boolean = false,
                                                       val roles: List<String> = listOf()) {
}


class ArticleListingHandler(val request: BanyanRequest) : BaseHandler<BanyanRequest, BanyanResponse> {

    override suspend fun execute(): BanyanResponse {
        return BanyanResponse("")
    }
}

interface BaseHandler<RouteRequest, RouteResponse> {
    suspend fun execute(): RouteResponse
}


data class BanyanRequest(val whatever: String = "")
data class BanyanResponse(val whatever: String = "")


private fun parseAuthorizationHeaderToToken(authHeader: String): String {
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ""
    }
    return authHeader.substring(7, authHeader.length)
}
