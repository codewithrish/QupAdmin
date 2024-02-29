package app.qup.network.data.remote.dto

import app.qup.network.domain.Token

data class TokenResponseDto(
    val access_token: String?,
    val token_type: String?,
    val refresh_token: String?,
    val scope: String?,
)

fun TokenResponseDto.toToken(): Token {
    return Token(
        accessToken = access_token ?: "",
        tokenType = token_type ?: "",
        refreshToken = refresh_token ?: "",
        scope = scope ?: ""
    )
}