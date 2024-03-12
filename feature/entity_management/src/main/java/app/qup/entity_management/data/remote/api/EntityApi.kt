package app.qup.entity_management.data.remote.api

import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.data.remote.dto.response.AllEntitiesResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityAccoladeResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityTypeResponseDto
import app.qup.entity_management.data.remote.dto.response.FacilityResponseDto
import app.qup.entity_management.data.remote.dto.response.InsuranceCompanyResponseDto
import app.qup.entity_management.data.remote.dto.response.ServiceResponseDto
import app.qup.entity_management.data.remote.dto.response.SpecialityResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EntityApi {
    @GET("provider-service/entity/")
    suspend fun getAllEntities(@Query("page") page: Int, @Query("size") size: Int): Response<AllEntitiesResponseDto>
    @POST("provider-service/entity/by-name")
    suspend fun searchEntity(@Body entityNames: List<String>, @Query("page") page: Int, @Query("size") size: Int): Response<AllEntitiesResponseDto>
    @POST("provider-service/entity/")
    suspend fun addEntity(@Body addEntityRequestDto: AddEntityRequestDto): Response<EntityResponseDto>
    @GET("master-service/master/entity/types")
    suspend fun getEntityTypes(): Response<List<EntityTypeResponseDto>>
    @GET("master-service/master/entity/insurance-comapnies")
    suspend fun getInsuranceCompanies(): Response<List<InsuranceCompanyResponseDto>>
    @GET("master-service/master/entity/facilities/active")
    suspend fun getFacilities(): Response<List<FacilityResponseDto>>
    @GET("master-service/master/entity/services/active")
    suspend fun getServices(): Response<List<ServiceResponseDto>>
    @GET("master-service/master/entity/accolades/active")
    suspend fun getAccolades(): Response<List<EntityAccoladeResponseDto>>
    @GET("master-service/master/doctor/speciality/active")
    suspend fun getSpecialities(): Response<List<SpecialityResponseDto>>
}