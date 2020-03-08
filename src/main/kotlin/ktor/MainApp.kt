package ktor

import com.fasterxml.jackson.databind.SerializationFeature
import commons.*
import commons.BadRequestException
import commons.NotFoundException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages) {
        registerExceptions()
    }
    routing {
        appRoutes()

        runBlocking {
            ApplicationRoutes.allRoutes()
        }

        applicationRoutes()
    }
}

fun main() {
    embeddedServer(Netty, 8080, watchPaths = listOf("MainAppKt"), module = Application::module).start()
}

fun StatusPages.Configuration.registerExceptions() {
    exception<BadRequestException> { cause ->
        call.respond(HttpStatusCode.BadRequest)
    }
    exception<UnauthorizedException> { cause ->
        call.respond(HttpStatusCode.Unauthorized)
    }
    exception<ForbiddenException> { cause ->
        call.respond(HttpStatusCode.Forbidden)
    }
    exception<NotFoundException> { cause ->
        call.respond(HttpStatusCode.NotFound)
    }
    exception<ConflictException> { cause ->
        call.respond(HttpStatusCode.Conflict)
    }
}
