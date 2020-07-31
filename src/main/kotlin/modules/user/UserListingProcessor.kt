package modules.user

import commons.BaseProcessor

class UserListingProcessor(private val repo: UserRepoInterface) : BaseProcessor<List<UserListingResponse>>() {

    override fun validate() {}

    override suspend fun process(): List<UserListingResponse> =
            repo.list().map { user -> UserListingResponse(user._id, user.name, user.email) }

}
