package app.qup.entity_management.data.remote.api

import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.data.remote.dto.response.AllEntitiesResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityResponseDto
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
}