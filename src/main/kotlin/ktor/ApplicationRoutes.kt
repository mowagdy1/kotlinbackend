package ktor

import commons.AuthTokenManagerJWT
import commons.TokenParams
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.util.pipeline.PipelineContext
import modules.user.UserRepoImpl
import modules.user.UserService

object ApplicationRoutes {
    val endpoints: MutableList<SingleEndpoint<Any, Any>> = mutableListOf()
    fun registerRoute(endpoint: SingleEndpoint<Any, Any>) {
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handlingRequest(endpoint: SingleEndpoint<Any, Any>) {
    if (endpoint.auth) {
        val authHeader = call.request.header(HttpHeaders.Authorization)
        if (authHeader is String && authHeader !== "") {
            endpoint.parsedToken = AuthTokenManagerJWT().parseToken(parseAuthorizationHeaderToToken(authHeader))
            call.respond(HttpStatusCode.OK, endpoint.execute())
        } else {
            call.respond(HttpStatusCode.Unauthorized, "auth is required")
        }
    }
    call.respond(HttpStatusCode.OK, endpoint.handler.execute())
}


suspend fun ApplicationRoutes.allRoutes() {

    registerRoute(SingleEndpoint(HttpMethod.Get, "", ArticleListingHandler()))


    //registerRoute(SingleEndpoint(HttpMethod.Get, "", BtnganRequest(), BtnganResponse()))


}


open class SingleEndpoint<RouteRequest : Any, RouteResponse : Any>(val method: HttpMethod,
                                                                   val route: String,
                                                                   val handler: BaseHandler,
                                                                //val routeRequest: RouteRequest,
                                                                //val routeResponse: RouteResponse,
                                                                   val auth: Boolean = false,
                                                                   val roles: List<String> = listOf()) {

    var parsedToken: TokenParams = TokenParams.blank()

    open fun execute() {}
}


//class ArticleListingEndpoint : SingleEndpoint<BtnganRequest, BtnganResponse>(HttpMethod.Get,
//        "articles") {
//    override fun execute() {
//        //
//    }
//}

class ArticleListingHandler : BaseHandler {
    override fun execute() {
        //
    }
}

interface BaseHandler {
    fun execute()
}


// for testing

data class Test1Request(val whatever: String = "")
data class Test1Response(val whatever: String = "")

data class BtnganRequest(val whatever: String = "")
data class BtnganResponse(val whatever: String = "")


private fun parseAuthorizationHeaderToToken(authHeader: String): String {
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ""
    }
    return authHeader.substring(7, authHeader.length)
}
