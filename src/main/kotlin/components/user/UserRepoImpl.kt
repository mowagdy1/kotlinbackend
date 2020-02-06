package components.user

import mongodb.MongoDb

const val USERS_COLLECTION = "users2"

class UserRepoImpl : UserRepoInterface {

    override suspend fun list(): List<User> = MongoDb.getDatabase().getCollection<User>(USERS_COLLECTION).find().toList()


    override suspend fun findById(_id: String): User {
        return User("3333", "Mooo", "email")
    }

    override suspend fun insert(name: String, email: String) {
        MongoDb.getDatabase().getCollection<User>("users2").insertOne(User(name = name, email = email))
    }

    override suspend fun update(_id: String, name: String, address: String) {

    }

    override suspend fun delete(_id: String) {

    }
}
