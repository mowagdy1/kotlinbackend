package modules.user

import commons.NotFoundException
import mongodb.MongoDb
import org.litote.kmongo.eq
import java.util.*

const val USERS_COLLECTION = "users2"

class UserRepoImpl : UserRepoInterface {
    private val userCollection = MongoDb.getDatabase().getCollection<User>(USERS_COLLECTION)

    override suspend fun list(): List<User> = userCollection.find().toList()

    override suspend fun findById(_id: String): User {
        val user = userCollection.findOne(User::_id eq _id)
        if (user != null) {
            return user
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun insert(name: String, email: String) {
        userCollection.insertOne(User(_id = UUID.randomUUID().toString(), name = name, email = email))
    }

    override suspend fun update(_id: String, name: String, email: String) {
        userCollection.replaceOneById(_id, User(name = name, email = email))
    }

    override suspend fun delete(_id: String) {

    }
}
