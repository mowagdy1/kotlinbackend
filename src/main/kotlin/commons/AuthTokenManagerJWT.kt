package commons

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*

private const val validityInMs = 36_000_00 * 10 // 10 hours

class AuthTokenManagerJWT(private val secret: String = "zAP5MBA4B4Ijz0MZaS48", private val issuer: String = "mowagdy") : AuthTokenManager {

    override fun generateToken(tokenParams: TokenParams): String = Jwts.builder()
            .setIssuer(issuer)
            .setSubject("Authentication")
            .claim(TokenParams::userId.name, tokenParams.userId)
            .claim(TokenParams::roles.name, tokenParams.roles)
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .setExpiration(getExpiration())
            .compact()

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

    override fun parseToken(token: String): TokenParams {
        try {

            val jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray()))
                    .build()
                    .parseClaimsJws(token);

            val roles: MutableList<String> = mutableListOf()
            val rolesJws = jws.body[TokenParams::roles.name]
            if (rolesJws is List<*>) {
                rolesJws.forEach {
                    if (it is String) roles += it
                }
            }

            return TokenParams(userId = jws.body[TokenParams::userId.name].toString(), roles = roles)

        } catch (ex: JwtException) {

            return TokenParams(userId = "", roles = mutableListOf())
        }
    }

    override fun getUserId(token: String): String = parseToken(token).userId

}
