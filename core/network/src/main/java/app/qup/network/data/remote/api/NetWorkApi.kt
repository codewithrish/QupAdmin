package app.qup.network.data.remote.api

import app.qup.network.data.remote.dto.TokenResponseDto
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface NetWorkApi {
    @FormUrlEncoded
    @POST("/userauth/oauth/token")
    suspend fun refreshToken(@Header("Authorization") encodedString: String, @FieldMap params: HashMap<String, String>): Response<TokenResponseDto>
}