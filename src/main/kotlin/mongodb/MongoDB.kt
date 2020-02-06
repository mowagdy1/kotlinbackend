package mongodb

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

const val DATABASE_NAME: String = "sample_app"

object MongoDb {
    private var kmongo: CoroutineClient? = null
    private var kdatabase: CoroutineDatabase? = null

    fun getDatabase(): CoroutineDatabase {
        if (kdatabase == null) {
            connect()
            database()
        }
        return kdatabase!!
    }

    private fun connect() {
        try {
            kmongo = KMongo.createClient().coroutine // "createClient" without parameters refers to the default => (host=localhost & port=27017)
            println("MongoClient connected")

        } catch (e: Exception) {
            kmongo?.close()
            println("MongoClient failed to connect: $e")
        }
    }

    private fun database() {
        kdatabase = kmongo?.getDatabase(DATABASE_NAME)
    }
}
