package app.qup.authentication.data.remote.api

import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.data.remote.dto.response.ResendOtpResponseDto
import app.qup.authentication.data.remote.dto.response.SignInResponseDto
import app.qup.authentication.data.remote.dto.response.VerifyOtpResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApi {
    @POST("userauth/users/sign-in/{mobileNumber}")
    suspend fun signIn(@Path("mobileNumber") mobileNumber: String, @Body signInRequestDto: SignInRequestDto): Response<SignInResponseDto>
    @POST("userauth/oauth/token")
    suspend fun verifyOtp(
        @Header("Authorization") encodedString: String,
        @Query("grant_type") grantType: String,
        @Query("scope") scope: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<VerifyOtpResponseDto>
    @GET("userauth/users/regenerate-otp/{mobileNumber}")
    suspend fun resendOtp(@Path("mobileNumber")mobileNumber: String): Response<ResendOtpResponseDto>
}