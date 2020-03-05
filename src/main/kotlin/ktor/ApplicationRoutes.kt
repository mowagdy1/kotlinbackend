package ktor

import io.ktor.http.HttpMethod
import io.ktor.routing.Route
import io.ktor.routing.route

object ApplicationRoutes {

    val routes: MutableList<SingleRoute<Any, Any>> = mutableListOf()

    fun registerRoute(route: SingleRoute<Any, Any>) {
        routes.add(route)
    }
}


fun Route.applicationRoutes() {

    ApplicationRoutes.routes.forEach {
        route(it.path, it.method) {

            if (it.auth) {

            }


        }
    }


}


fun ApplicationRoutes.allRoutes() {

    registerRoute(route = SingleRoute(method = HttpMethod.Get, path = "", routeRequest = Test1Request(), routeResponse = Test1Response()))
    registerRoute(route = SingleRoute(method = HttpMethod.Get, path = "", routeRequest = BtnganRequest(), routeResponse = BtnganResponse()))


}


data class SingleRoute<RouteRequest : Any, RouteResponse : Any>(val method: HttpMethod,
                                                                val path: String,
                                                                val routeRequest: RouteRequest,
                                                                val routeResponse: RouteResponse,
                                                                val auth: Boolean = false,
                                                                val roles: List<String> = listOf())

interface Service {}

// for testing
data class Test1Request(val whatever: String = "")

data class Test1Response(val whatever: String = "")


data class BtnganRequest(val whatever: String = "")
data class BtnganResponse(val whatever: String = "")
