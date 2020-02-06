package ktor

import com.fasterxml.jackson.databind.SerializationFeature
import components.user.userRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DefaultHeaders)
    install(CallLogging)
    routing {
        userRoutes()
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("MainAppKt"), module = Application::module).start()
}
