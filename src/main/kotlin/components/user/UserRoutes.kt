package components.user

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.get
import io.ktor.routing.post
import mongodb.MongoDb

fun Route.userRoutes() {
    route("/users") {

        get("") {
            call.respondText("all users", ContentType.Text.Html)
        }

        get("/list") {
            val users = MongoDb.getDatabase()
                    .getCollection<User>("users2")
                    .find()
                    .toList()
            call.respond(HttpStatusCode.OK, users)
        }

        post<UserRegisterRequest>("/register") { request ->
            val user = User(
                    name = request.name,
                    email = request.email
            )
            MongoDb.getDatabase()
                    .getCollection<User>("users2")
                    .insertOne(user)
            call.respond(HttpStatusCode.OK)
        }

    }
}
