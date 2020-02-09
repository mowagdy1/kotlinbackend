package modules.user

import mongodb.MongoDb
import java.util.*

const val USERS_COLLECTION = "users2"

class UserRepoImpl : UserRepoInterface {
    private val userCollection = MongoDb.getDatabase().getCollection<User>(USERS_COLLECTION)

    override suspend fun list(): List<User> = userCollection.find().toList()

    override suspend fun findById(_id: String): User {
        return User("3333", "Mooo", "email")
    }

    override suspend fun insert(name: String, email: String) {
        userCollection.insertOne(User(_id = UUID.randomUUID().toString(), name = name, email = email))
    }

    override suspend fun update(_id: String, name: String, address: String) {

    }

    override suspend fun delete(_id: String) {

    }
}
