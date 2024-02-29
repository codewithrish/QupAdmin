package app.qup.network.domain

data class Token (
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String,
    val scope: String,
)