package app.qup.authentication.domain.model

data class Tokens(
    val accessToken: String,
    val expiresIn: Int,
    val jti: String,
    val refreshToken: String,
    val scope: String,
    val tokenType: String
)
