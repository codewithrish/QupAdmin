package app.qup.entity_management.domain.repository

import androidx.paging.PagingData
import app.qup.entity_management.data.remote.dto.general.EntityResource
import app.qup.entity_management.data.remote.dto.request.EntityRequestDto
import app.qup.entity_management.data.remote.dto.response.EntityAccoladeResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityResponseDto
import app.qup.entity_management.data.remote.dto.response.EntityTypeResponseDto
import app.qup.entity_management.data.remote.dto.response.FacilityResponseDto
import app.qup.entity_management.data.remote.dto.response.InsuranceCompanyResponseDto
import app.qup.entity_management.data.remote.dto.response.ServiceResponseDto
import app.qup.entity_management.data.remote.dto.response.SpecialityResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface EntityRepository {
    suspend fun getAllEntities(size: Int): Flow<PagingData<EntityResource>>
    suspend fun searchEntity(entityNames: List<String>, size: Int): Flow<PagingData<EntityResource>>
    suspend fun addEntity(entityRequestDto: EntityRequestDto): Response<EntityResponseDto>
    suspend fun getEntityById(id: String): Response<EntityResponseDto>
    suspend fun updateEntityById(id: String, entityRequestDto: EntityRequestDto): Response<EntityResponseDto>
     suspend fun getEntityTypes(): Response<List<EntityTypeResponseDto>>
    suspend fun getInsuranceCompanies(): Response<List<InsuranceCompanyResponseDto>>
    suspend fun getFacilities(): Response<List<FacilityResponseDto>>
    suspend fun getServices(): Response<List<ServiceResponseDto>>
    suspend fun getAccolades(): Response<List<EntityAccoladeResponseDto>>
    suspend fun getSpecialities(): Response<List<SpecialityResponseDto>>
}