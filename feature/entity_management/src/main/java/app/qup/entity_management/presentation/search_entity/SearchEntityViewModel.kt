package app.qup.entity_management.presentation.search_entity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.qup.entity_management.domain.model.EntityR
import app.qup.entity_management.domain.use_case.GetAllEntitiesUseCase
import app.qup.entity_management.domain.use_case.SearchEntitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchEntityViewModel @Inject constructor(
    private val getAllEntitiesUseCase: GetAllEntitiesUseCase,
    private val searchEntitiesUseCase: SearchEntitiesUseCase,
) : ViewModel() {
    private lateinit var _entities: Flow<PagingData<EntityR>>
    val entities: Flow<PagingData<EntityR>>
        get() = _entities

    private lateinit var _searchedEntities: Flow<PagingData<EntityR>>
    val searchedEntities: Flow<PagingData<EntityR>>
        get() = _searchedEntities

    fun getAllEntities(
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _entities = getAllEntitiesUseCase(size).cachedIn(this)
        }
    }

    fun searchEntities(
        entityNames: List<String>,
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchedEntities = searchEntitiesUseCase(entityNames, size).cachedIn(this)
        }
    }
}