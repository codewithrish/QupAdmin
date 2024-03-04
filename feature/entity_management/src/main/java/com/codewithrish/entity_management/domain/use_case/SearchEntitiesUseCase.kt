package com.codewithrish.entity_management.domain.use_case

import androidx.paging.PagingData
import androidx.paging.map
import com.codewithrish.entity_management.data.remote.dto.general.toEntity
import com.codewithrish.entity_management.domain.model.Entity
import com.codewithrish.entity_management.domain.repository.EntityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchEntitiesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    suspend operator fun invoke(
        entityNames: List<String>,
        size: Int
    ) : Flow<PagingData<Entity>> = entityRepository.searchEntity(entityNames, size).map { pagingData ->
        pagingData.map { it1 ->
            it1.toEntity()
        }
    }
}