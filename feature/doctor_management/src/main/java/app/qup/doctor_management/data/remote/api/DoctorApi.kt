package app.qup.doctor_management.data.remote.api

import app.qup.doctor_management.data.remote.dto.general.DoctorSearchResource
import app.qup.doctor_management.data.remote.dto.response.AllDoctorsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorApi {
    @GET("provider-service/doctor/")
    suspend fun getAllDoctors(@Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctor/by-name/{doctorName}")
    suspend fun searchDoctorByName(@Path("doctorName") doctorName: String, @Query("page") page: Int, @Query("size") size: Int): Response<AllDoctorsResponseDto>
    @GET("provider-service/doctor/{mobileNumber}")
    suspend fun searchDoctorByNumber(@Path("mobileNumber") mobileNumber: String): Response<DoctorSearchResource>
}