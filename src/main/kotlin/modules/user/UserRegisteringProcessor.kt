package modules.user

import commons.BaseProcessor

class UserRegisteringProcessor(private val request: UserRegisterRequest,
                               private val repo: UserRepoInterface) : BaseProcessor<EmptyResponse>() {

    override fun validate() {}

    override suspend fun process(): EmptyResponse {
        repo.insert(name = request.name, email = request.email)

        return EmptyResponse()
    }

}
