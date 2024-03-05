package app.qup.reception_management.presentation.add_reception

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.domain.use_case.AddReceptionState
import app.qup.reception_management.domain.use_case.AddReceptionUseCase
import app.qup.reception_management.domain.use_case.GetGendersState
import app.qup.reception_management.domain.use_case.GetGendersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddReceptionViewModel @Inject constructor(
    private val addReceptionUseCase: AddReceptionUseCase,
    private val getGendersUseCase: GetGendersUseCase
) : ViewModel() {
    private val _addReception: MutableLiveData<AddReceptionState> = MutableLiveData()
    val addReception: LiveData<AddReceptionState>
        get() = _addReception

    private val _genders: MutableLiveData<GetGendersState> = MutableLiveData()
    val genders: LiveData<GetGendersState>
        get() = _genders

    init {
        getGenders()
    }

    fun addReception(
        addReceptionRequestDto: AddReceptionRequestDto
    ) {
        addReceptionUseCase(addReceptionRequestDto).onEach {
            _addReception.postValue(it)
        }.launchIn(viewModelScope)
    }

    private fun getGenders() {
        getGendersUseCase().onEach {
            _genders.postValue(it)
        }.launchIn(viewModelScope)
    }
}