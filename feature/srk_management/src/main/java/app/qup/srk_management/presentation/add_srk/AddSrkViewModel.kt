package app.qup.srk_management.presentation.add_srk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.srk_management.data.remote.dto.request.AddSrkRequestDto
import app.qup.srk_management.domain.use_case.AddSrkState
import app.qup.srk_management.domain.use_case.AddSrkUseCase
import app.qup.srk_management.domain.use_case.GetGendersState
import app.qup.srk_management.domain.use_case.GetGendersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddSrkViewModel @Inject constructor(
    private val addSrkUseCase: AddSrkUseCase,
    private val getGendersUseCase: GetGendersUseCase
) : ViewModel() {
    private val _addSrk: MutableLiveData<AddSrkState> = MutableLiveData()
    val addSrk: LiveData<AddSrkState>
        get() = _addSrk

    private val _genders: MutableLiveData<GetGendersState> = MutableLiveData()
    val genders: LiveData<GetGendersState>
        get() = _genders

    init {
        getGenders()
    }

    fun addSrk(
        addSrkRequestDto: AddSrkRequestDto
    ) {
        addSrkUseCase(addSrkRequestDto).onEach {
            _addSrk.postValue(it)
        }.launchIn(viewModelScope)
    }

    private fun getGenders() {
        getGendersUseCase().onEach {
            _genders.postValue(it)
        }.launchIn(viewModelScope)
    }
}