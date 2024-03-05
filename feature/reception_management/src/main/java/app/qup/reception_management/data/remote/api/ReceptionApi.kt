package app.qup.reception_management.data.remote.api

import app.qup.reception_management.data.remote.dto.general.ReceptionResource
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.data.remote.dto.response.AllReceptionsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceptionApi {
    @GET("userauth/users/with-role/{role}")
    suspend fun getAllReceptions(@Path("role") role: String, @Query("page") page: Int, @Query("size") size: Int): Response<AllReceptionsResponseDto>
    @POST("userauth/users/with-role-and-name")
    suspend fun searchReceptionByName(@Body searchUserRequestDto: SearchUserRequestDto): Response<List<ReceptionResource>>
    @GET("userauth/users/with-role/{role}/mobile/{mobileNumber}")
    suspend fun searchReceptionByNumber(@Path("role") role: String, @Path("mobileNumber") mobileNumber: String): Response<ReceptionResource>
    @POST("userauth/users/")
    suspend fun addReception(@Body addReceptionRequestDto: AddReceptionRequestDto): Response<ReceptionResource>
    @GET("master-service/master/common/gender")
    suspend fun getGenders(): Response<List<String>>
}