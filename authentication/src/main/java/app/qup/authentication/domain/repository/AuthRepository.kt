package app.qup.authentication.domain.repository

import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.data.remote.dto.response.ResendOtpResponseDto
import app.qup.authentication.data.remote.dto.response.SignInResponseDto
import app.qup.authentication.data.remote.dto.response.VerifyOtpResponseDto
import retrofit2.Response

interface AuthRepository {
    suspend fun signIn(mobileNumber: String, signInRequestDto: SignInRequestDto): Response<SignInResponseDto>
    suspend fun verifyOtp(
        encodedString: String,
        grantType: String,
        scope: String,
        username: String,
        password: String
    ): Response<VerifyOtpResponseDto>
    suspend fun resendOtp(mobileNumber: String): Response<ResendOtpResponseDto>
}