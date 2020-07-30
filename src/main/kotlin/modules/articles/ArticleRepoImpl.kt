package modules.articles

import commons.NotFoundException
import mongodb.MongoDb
import org.litote.kmongo.eq
import java.util.*

const val ARTICLES_COLLECTION = "articles2"

class ArticleRepoImpl : ArticleRepoInterface {
    private val articleCollection = MongoDb.getDatabase().getCollection<Article>(ARTICLES_COLLECTION)

    override suspend fun list(): List<Article> = articleCollection.find().toList()

    override suspend fun findById(_id: String): Article {
        val article = articleCollection.findOne(Article::_id eq _id)
        if (article != null) {
            return article
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun insert(content: String) {
        articleCollection.insertOne(Article(_id = UUID.randomUUID().toString(), content = content))
    }

    override suspend fun update(_id: String, content: String) {
        articleCollection.replaceOneById(_id, Article(content = content))
    }

    override suspend fun delete(_id: String) {

    }
}
