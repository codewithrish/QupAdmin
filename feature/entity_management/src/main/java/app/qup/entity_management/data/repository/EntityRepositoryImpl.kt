package app.qup.entity_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.qup.entity_management.data.paging.GetAllEntitiesPagingData
import app.qup.entity_management.data.paging.SearchEntitiesPagingData
import app.qup.entity_management.data.remote.api.EntityApi
import app.qup.entity_management.data.remote.dto.general.EntityResource
import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.data.remote.dto.response.EntityResponseDto
import app.qup.entity_management.domain.repository.EntityRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class EntityRepositoryImpl @Inject constructor(
    private val entityApi: EntityApi
) : EntityRepository {
    override suspend fun getAllEntities(size: Int): Flow<PagingData<EntityResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            GetAllEntitiesPagingData(
                entityApi,
                size
            )
        }
    ).flow

    override suspend fun searchEntity(
        entityNames: List<String>,
        size: Int
    ): Flow<PagingData<EntityResource>> = Pager(
        config = PagingConfig(pageSize = size, maxSize = size * 4),
        pagingSourceFactory = {
            SearchEntitiesPagingData(
                entityApi,
                entityNames,
                size
            )
        }
    ).flow

    override suspend fun addEntity(addEntityRequestDto: AddEntityRequestDto): Response<EntityResponseDto> {
        return entityApi.addEntity(addEntityRequestDto)
    }
}