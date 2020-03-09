package ktor

import commons.AuthTokenManagerJWT
import commons.BadRequestException
import commons.TokenParams
import io.ktor.application.call
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import modules.user.UserRegisterRequest
import modules.user.UserRepoImpl
import modules.user.UserService
import modules.user.UserUpdateRequest

fun Route.appRoutes() {
    route("/users") {
//        get("") {
//            call.respond(HttpStatusCode.OK, UserService(UserRepoImpl()).list())
//        }

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



    post("validate-token") {

        val authHeader = call.request.header(HttpHeaders.Authorization)

        if (authHeader is String) {

            val parsed = parseAuthorizationHeaderToToken(authHeader)

            val authTokenManagerJWT = AuthTokenManagerJWT()

            val parsedToken = authTokenManagerJWT.parseToken(parsed)


            call.respond(HttpStatusCode.OK, parsedToken)

        }

        call.respond(HttpStatusCode.OK, "mfeesh")
    }

    get("get-token") {
        val authTokenManagerJWT = AuthTokenManagerJWT()
        val token = authTokenManagerJWT.generateToken(TokenParams("mo", listOf("Customer")))
        call.respond(HttpStatusCode.OK, token)
    }


}

private fun parseAuthorizationHeaderToToken(authHeader: String): String {
    if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ""
    }
    return authHeader.substring(7, authHeader.length)
}
