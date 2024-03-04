package com.codewithrish.entity_management.domain.repository

import androidx.paging.PagingData
import com.codewithrish.entity_management.data.remote.dto.general.EntityResource
import kotlinx.coroutines.flow.Flow

interface EntityRepository {
    suspend fun getAllEntities(size: Int): Flow<PagingData<EntityResource>>
    suspend fun searchEntity(entityNames: List<String>, size: Int): Flow<PagingData<EntityResource>>
}