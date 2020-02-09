package ktor

import commons.BadRequestException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.get
import io.ktor.routing.post
import modules.user.UserRegisterRequest
import modules.user.UserRepoImpl
import modules.user.UserService

fun Route.appRoutes() {
    route("/users") {
        get("") {
            call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).list())
        }
        get("/{id}") {
            val id: String? = call.parameters["id"]
            if (id != null)
                call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).findById(id))
            else throw BadRequestException()
        }
        post<UserRegisterRequest>("register") { request ->
            call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).register(request))
        }
    }
}
