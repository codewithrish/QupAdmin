package app.qup.doctor_management.presentation.add_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.domain.use_case.AddDoctorState
import app.qup.doctor_management.domain.use_case.AddDoctorUseCase
import app.qup.doctor_management.domain.use_case.GetGendersState
import app.qup.doctor_management.domain.use_case.GetGendersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddDoctorViewModel @Inject constructor(
    private val addDoctorUseCase: AddDoctorUseCase,
    private val getGendersUseCase: GetGendersUseCase
) : ViewModel() {
    private val _addDoctor: MutableLiveData<AddDoctorState> = MutableLiveData()
    val addDoctor: LiveData<AddDoctorState>
        get() = _addDoctor

    private val _genders: MutableLiveData<GetGendersState> = MutableLiveData()
    val genders: LiveData<GetGendersState>
        get() = _genders

    fun addDoctor(
        doctorRequestDto: DoctorRequestDto
    ) {
        addDoctorUseCase(doctorRequestDto).onEach {
            _addDoctor.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getGenders() {
        getGendersUseCase().onEach {
            _genders.postValue(it)
        }.launchIn(viewModelScope)
    }
}