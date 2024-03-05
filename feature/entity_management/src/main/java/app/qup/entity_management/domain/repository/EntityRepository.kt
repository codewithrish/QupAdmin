package app.qup.entity_management.domain.repository

import androidx.paging.PagingData
import app.qup.entity_management.data.remote.dto.general.EntityResource
import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.data.remote.dto.response.EntityResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface EntityRepository {
    suspend fun getAllEntities(size: Int): Flow<PagingData<EntityResource>>
    suspend fun searchEntity(entityNames: List<String>, size: Int): Flow<PagingData<EntityResource>>
    suspend fun addEntity(addEntityRequestDto: AddEntityRequestDto): Response<EntityResponseDto>
}