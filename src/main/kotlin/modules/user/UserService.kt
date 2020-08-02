package modules.user

import base.generateUniqueId

class UserService(private val repo: UserRepoInterface) {

    suspend fun list(): List<UserListingResponse> = repo.list().map { user -> UserListingResponse(user._id, user.name, user.email) }

    suspend fun findById(_id: Long): SingleUserResponse {
        val user = repo.findById(_id)
        return SingleUserResponse(user._id, user.name, user.email)
    }

    suspend fun register(request: UserRegisterRequest) {
        repo.insert(User(_id = generateUniqueId(), name = request.name, email = request.email))
    }

    suspend fun update(request: UserUpdateRequest, _id: Long) {
        repo.update(_id = _id, name = request.name, email = request.email)
    }
}
