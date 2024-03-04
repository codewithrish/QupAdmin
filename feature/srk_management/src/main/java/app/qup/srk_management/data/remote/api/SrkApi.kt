package app.qup.srk_management.data.remote.api

import app.qup.srk_management.data.remote.dto.general.SrkResource
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.data.remote.dto.response.AllSrkResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SrkApi {
    @GET("userauth/users/with-role/{role}")
    suspend fun getAllSrk(@Path("role") role: String, @Query("page") page: Int, @Query("size") size: Int): Response<AllSrkResponseDto>
    @POST("userauth/users/with-role-and-name")
    suspend fun searchSrkByName(@Body searchUserRequestDto: SearchUserRequestDto): Response<List<SrkResource>>
    @GET("userauth/users/with-role/{role}/mobile/{mobileNumber}")
    suspend fun searchReceptionByNumber(@Path("role") role: String, @Path("mobileNumber") mobileNumber: String): Response<SrkResource>
}