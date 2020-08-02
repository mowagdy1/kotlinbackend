package modules.articles

import base.BaseProcessor

class ArticleListingProcessor(private val repo: ArticleRepoInterface) : BaseProcessor<BanyanResponse>() {

    override fun validate() {}

    override suspend fun process(): BanyanResponse {
        return BanyanResponse("Ay 7aga")
    }
}

data class BanyanResponse(val whatever: String = "")
