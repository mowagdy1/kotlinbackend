package modules.user

import ktor.BaseHandler

class UserListingHandler(private val request: EmptyRequest,
                         private val repo: UserRepoInterface) : BaseHandler<EmptyRequest, List<UserListingResponse>> {

    override suspend fun execute(): List<UserListingResponse> =
            repo.list().map { user -> UserListingResponse(user._id, user.name, user.email) }
}
