package modules.user

import ktor.Service

class UserService(private val repo: UserRepoInterface) : Service {

    suspend fun list(): List<UserListingResponse> = repo.list().map { user -> UserListingResponse(user._id, user.name, user.email) }

    suspend fun findById(_id: String): SingleUserResponse {
        val user = repo.findById(_id)
        return SingleUserResponse(user._id, user.name, user.email)
    }

    suspend fun register(request: UserRegisterRequest) {
        repo.insert(name = request.name, email = request.email)
    }

    suspend fun update(request: UserUpdateRequest, _id: String) {
        repo.update(_id = _id, name = request.name, email = request.email)
    }
}
