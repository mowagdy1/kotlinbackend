package ktor

import commons.ApplicationRoutes
import commons.RouteMethod
import commons.SingleRoute
import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext
import modules.articles.ArticleListingProcessor
import modules.articles.ArticleRepoImpl
import modules.articles.BanyanResponse
import modules.user.*

fun ApplicationRoutes.registerAllRoutes() {

    registerRoute(SingleRoute(RouteMethod.GET, "p", ArticleListingHandler()))

    registerRoute(SingleRoute(RouteMethod.GET, "users", UserListingHandler()))
    registerRoute(SingleRoute(RouteMethod.POST, "users/register", UserRegisteringHandler()))

}


class ArticleListingHandler : BaseApiHandler<EmptyRequest, BanyanResponse>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            ArticleListingProcessor(ArticleRepoImpl())
}

class UserListingHandler : BaseApiHandler<EmptyRequest, List<UserListingResponse>>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            UserListingProcessor(UserRepoImpl())
}

class UserRegisteringHandler : BaseApiHandler<UserRegisterRequest, EmptyResponse>() {
    override suspend fun specifyProcessor(ctx: PipelineContext<Unit, ApplicationCall>) =
            UserRegisteringProcessor(readRequest(ctx), UserRepoImpl())
}
