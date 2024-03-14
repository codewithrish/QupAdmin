package app.qup.entity_management.presentation.search_entity.entity_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.entity_management.domain.use_case.GetEntityByIdState
import app.qup.entity_management.domain.use_case.GetEntityByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EntityDetailsViewModel @Inject constructor(
    private val getEntityByIdUseCase: GetEntityByIdUseCase
) : ViewModel() {
    private val _getEntity: MutableLiveData<GetEntityByIdState> = MutableLiveData()
    val getEntity: LiveData<GetEntityByIdState>
        get() = _getEntity


    fun getEntityById(
        id: String,
    ) {
        getEntityByIdUseCase(id).onEach {
            _getEntity.postValue(it)
        }.launchIn(viewModelScope)
    }
}