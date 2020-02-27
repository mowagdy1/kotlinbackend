package ktor

import commons.BadRequestException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import modules.user.UserRegisterRequest
import modules.user.UserRepoImpl
import modules.user.UserService
import modules.user.UserUpdateRequest

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

        post("register") {

            val userRegisterRequest = call.receive<UserRegisterRequest>()

            call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).register(userRegisterRequest))
        }

        put("/{id}") {

            val id: String? = call.parameters["id"]
            val userUpdateRequest = call.receive<UserUpdateRequest>()

            if (id != null)
                call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).update(userUpdateRequest, id))
            else throw BadRequestException()
        }

    }
}
