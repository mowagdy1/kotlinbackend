package mongodb

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

const val DATABASE_NAME: String = "sample_app"

object MongoDb {
    private var client: CoroutineClient? = null
    private var database: CoroutineDatabase? = null

    fun getDatabase(): CoroutineDatabase {
        if (database == null) {
            connect()
            database()
        }
        return database!!
    }

    private fun connect() {
        try {
            //"createClient" without parameters refers to the default => (host=localhost & port=27017)
            client = KMongo.createClient().coroutine
            println("MongoClient connected")

        } catch (e: Exception) {
            client?.close()
            println("MongoClient failed to connect: $e")
        }
    }

    private fun database() {
        database = client?.getDatabase(DATABASE_NAME)
    }
}
