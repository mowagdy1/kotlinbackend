package modules.user

import base.NotFoundException
import mongodb.MongoDb
import org.litote.kmongo.eq

const val USERS_COLLECTION = "users"

class UserRepoImpl : UserRepoInterface {
    private val userCollection = MongoDb.getDatabase().getCollection<User>(USERS_COLLECTION)

    override suspend fun list(): List<User> = userCollection.find().toList()

    override suspend fun findById(_id: Long): User {
        val user = userCollection.findOne(User::_id eq _id)
        if (user != null) {
            return user
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun insert(user: User) {
        userCollection.insertOne(user)
    }

    override suspend fun update(_id: Long, name: String, email: String) {
        userCollection.replaceOneById(_id, User(name = name, email = email))
    }

    override suspend fun delete(_id: Long) {

    }
}
