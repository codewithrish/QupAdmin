package app.qup.entity_management.presentation.add_entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.domain.use_case.AddEntityState
import app.qup.entity_management.domain.use_case.AddEntityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddEntityViewModel @Inject constructor(
    private val addEntityUseCase: AddEntityUseCase
) : ViewModel() {
    private val _addEntity: MutableLiveData<AddEntityState> = MutableLiveData()
    val addEntity: LiveData<AddEntityState>
        get() = _addEntity

    fun addEntity(
        addEntityRequestDto: AddEntityRequestDto
    ) {
        addEntityUseCase(addEntityRequestDto).onEach {
            _addEntity.postValue(it)
        }.launchIn(viewModelScope)
    }
}