package modules.user

import base.generateUniqueId
import base.BaseProcessor
import base.EmptyResponse

class UserRegisteringProcessor(private val request: UserRegisterRequest,
                               private val repo: UserRepoInterface) : BaseProcessor<EmptyResponse>() {

    override fun validate() {}

    override suspend fun process(): EmptyResponse {
        repo.insert(User(_id = generateUniqueId(),
                name = request.name,
                email = request.email,
                createdAt = System.currentTimeMillis()))
        return EmptyResponse()
    }

}
