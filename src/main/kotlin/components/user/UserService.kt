package components.user

class UserService(private val repo: UserRepoInterface) {

    suspend fun list(): List<UserListingResponse> = repo.list().map { user -> UserListingResponse(user._id, user.name, user.email) }

    suspend fun register(request: UserRegisterRequest) {
        repo.insert(name = request.name, email = request.email)
    }

}