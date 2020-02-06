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

import org.litote.kmongo.reactivestreams.*  //NEEDED! import KMongo reactivestreams extensions
import org.litote.kmongo.coroutine.* //NEEDED! import KMongo coroutine extensions


val client = KMongo.createClient().coroutine //use coroutine extension
//val database = client.getDatabase("test") //normal java driver usage


fun Route.userRoutes() {

    route("/users") {

        get("") {
            call.respondText("all users", ContentType.Text.Html)
        }

        get("/list") {
            val users = client.getDatabase("sample_app")
                    .getCollection<User>("users2")
                    .find()
                    .toList()
            call.respond(HttpStatusCode.OK, users)
        }

        post<RegisterRequest>("/register") { request ->
            val user = User(
                    userName = request.userName,
                    password = request.password
            )
            client.getDatabase("sample_app")
                    .getCollection<User>("users2")
                    .insertOne(user)
            call.respond(HttpStatusCode.OK)
        }

    }

}
