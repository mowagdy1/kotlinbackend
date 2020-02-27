package commons

interface AuthTokenManager {
    fun generateToken(tokenParams: TokenParams): String
    fun parseToken(token: String): TokenParams
    fun getUserId(token: String): String
}

data class TokenParams(val userId: String, val roles: List<String>)
