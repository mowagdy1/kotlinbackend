package modules.articles

import ktor.BanyanResponse
import ktor.BaseProcessor

class ArticleListingProcessor(private val repo: ArticleRepoInterface) : BaseProcessor<BanyanResponse>() {

    override fun validate() {}

    override suspend fun process(): BanyanResponse {
        return BanyanResponse("Ay 7aga")
    }
}
