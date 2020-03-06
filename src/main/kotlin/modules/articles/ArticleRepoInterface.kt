package modules.articles

interface ArticleRepoInterface {
    suspend fun list(): List<Article>

    suspend fun findById(_id: String): Article

    suspend fun insert(content: String)

    suspend fun update(_id: String, content: String)

    suspend fun delete(_id: String)
}
