package modules.user

interface UserRepoInterface {
    suspend fun list(): List<User>

    suspend fun findById(_id: String): User

    suspend fun insert(name: String, email: String)

    suspend fun update(_id: String, name: String, email: String)

    suspend fun delete(_id: String)
}
