package modules.user

interface UserRepoInterface {
    suspend fun list(): List<User>

    suspend fun findById(_id: Long): User

    suspend fun insert(user: User)

    suspend fun update(_id: Long, name: String, email: String)

    suspend fun delete(_id: Long)
}
