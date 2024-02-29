package app.qup.authentication.data.remote.dto.response

import app.qup.authentication.domain.model.Tokens

data class VerifyOtpResponseDto(
    val access_token: String?,
    val expires_in: Int?,
    val jti: String?,
    val refresh_token: String?,
    val scope: String?,
    val token_type: String?
)
fun VerifyOtpResponseDto.toTokens(): Tokens {
    return Tokens(
        accessToken = access_token?:"",
        expiresIn = expires_in?:0,
        jti = jti?:"",
        refreshToken = refresh_token?:"",
        scope = scope?:"",
        tokenType = token_type?:""
    )
}