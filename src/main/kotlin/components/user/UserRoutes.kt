package components.user

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.get
import io.ktor.routing.post

fun Route.userRoutes() {
    val userRepoImpl = UserRepoImpl()
    route("/users") {

        get("") {
            call.respond(HttpStatusCode.OK, UserService(userRepoImpl).list())
        }

        post<UserRegisterRequest>("register") { request ->
            call.respond(HttpStatusCode.OK, UserService(userRepoImpl).register(request))
        }

    }
}
