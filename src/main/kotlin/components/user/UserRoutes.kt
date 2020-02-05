package components.user

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.userRoutes() {

    route("/users") {

        get("") {
            call.respondText("all users", ContentType.Text.Html)
        }

    }

}
