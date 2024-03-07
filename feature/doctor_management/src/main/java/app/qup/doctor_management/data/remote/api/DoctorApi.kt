package app.qup.doctor_management.data.remote.api

import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.response.SpecialityCategoryResponseDto
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.AccoladeResponseDto
import app.qup.doctor_management.data.remote.dto.response.AllDoctorsResponseDto
import app.qup.doctor_management.data.remote.dto.response.DegreeResponseDto
import app.qup.doctor_management.data.remote.dto.response.DoctorResponseDto
import app.qup.doctor_management.data.remote.dto.response.SpecialityResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorApi {
    @GET("provider-service/doctor/")
    suspend fun getAllDoctors(@Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctor/by-name/{doctorName}")
    suspend fun searchDoctorByName(@Path("doctorName") doctorName: String, @Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctor/{mobileNumber}")
    suspend fun searchDoctorByNumber(@Path("mobileNumber") mobileNumber: String): Response<DoctorSearchResource>
    @POST("provider-service/doctor/")
    suspend fun addDoctor(@Body doctorRequestDto: DoctorRequestDto): Response<DoctorResponseDto>
    @GET("master-service/master/common/gender")
    suspend fun getGenders(): Response<List<String>>
    @GET("master-service/master/doctor/degree/active")
    suspend fun getActiveDegreeMaster(): Response<List<DegreeResponseDto>>
    @GET("master-service/master/doctor/speciality/active")
    suspend fun getActiveSpecialityMaster(): Response<List<SpecialityResponseDto>>
    @GET("master-service/master/doctor/speciality-category/active")
    suspend fun getActiveSpecialityCategoryMaster(): Response<List<SpecialityCategoryResponseDto>>
    @GET("/master-service/master/doctor/accolades/active")
    suspend fun getActiveAccolades(): Response<List<AccoladeResponseDto>>
}