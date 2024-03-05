package app.qup.doctor_management.data.remote.api

import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.AllDoctorsResponseDto
import app.qup.doctor_management.data.remote.dto.response.DoctorResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorApi {
    @GET("provider-service/doctorR/")
    suspend fun getAllDoctors(@Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctorR/by-name/{doctorName}")
    suspend fun searchDoctorByName(@Path("doctorName") doctorName: String, @Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctorR/{mobileNumber}")
    suspend fun searchDoctorByNumber(@Path("mobileNumber") mobileNumber: String): Response<DoctorSearchResource>
    @POST("provider-service/doctor/")
    suspend fun addDoctor(@Body doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto>
    @GET("master-service/master/common/gender")
    suspend fun getGenders(): Response<List<String>>
}