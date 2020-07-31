package commons

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.util.pipeline.PipelineContext

object ApplicationRoutes {
    val routes: MutableList<SingleRoute> = mutableListOf()

    fun registerRoute(endpoint: SingleRoute) {
        routes.add(endpoint)
    }
}

interface JsonMapper {
    fun <T> toJson(obj: T): String
}

interface BaseRouteHandler {
    suspend fun handle(ctx: PipelineContext<Unit, ApplicationCall>)
}

open class SingleRoute(val method: HttpMethod,
                       val uri: String,
                       val handler: BaseRouteHandler,
                       val auth: Boolean = false,
                       val roles: List<String> = listOf())


abstract class BaseProcessor<Response> {
    abstract fun validate()
    abstract suspend fun process(): Response
    suspend fun execute(): Response {
        validate()
        return process()
    }
}
