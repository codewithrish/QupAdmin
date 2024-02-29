package app.qup.authentication.data.repository

import app.qup.authentication.data.remote.api.AuthApi
import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.data.remote.dto.response.ResendOtpResponseDto
import app.qup.authentication.data.remote.dto.response.SignInResponseDto
import app.qup.authentication.data.remote.dto.response.VerifyOtpResponseDto
import app.qup.authentication.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
): AuthRepository {
    override suspend fun signIn(mobileNumber: String, signInRequestDto: SignInRequestDto): Response<SignInResponseDto> {
        return authApi.signIn(mobileNumber, signInRequestDto)
    }
    override suspend fun verifyOtp(
        encodedString: String,
        grantType: String,
        scope: String,
        username: String,
        password: String
    ): Response<VerifyOtpResponseDto> {
        return authApi.verifyOtp(encodedString, grantType, scope, username, password)
    }
    override suspend fun resendOtp(mobileNumber: String): Response<ResendOtpResponseDto> {
        return authApi.resendOtp(mobileNumber)
    }
}