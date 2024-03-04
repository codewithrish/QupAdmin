package com.codewithrish.entity_management.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.codewithrish.entity_management.data.paging.GetAllEntitiesPagingData
import com.codewithrish.entity_management.data.paging.SearchEntitiesPagingData
import com.codewithrish.entity_management.data.remote.api.EntityApi
import com.codewithrish.entity_management.data.remote.dto.general.EntityResource
import com.codewithrish.entity_management.domain.repository.EntityRepository
import kotlinx.coroutines.flow.Flow
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
}