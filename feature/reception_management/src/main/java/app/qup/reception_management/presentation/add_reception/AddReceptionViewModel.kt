package app.qup.reception_management.presentation.add_reception

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.domain.use_case.ReceptionState
import app.qup.reception_management.domain.use_case.AddReceptionUseCase
import app.qup.reception_management.domain.use_case.CheckRoleEligibilityState
import app.qup.reception_management.domain.use_case.CheckRoleEligibilityUseCase
import app.qup.reception_management.domain.use_case.GetGendersState
import app.qup.reception_management.domain.use_case.GetGendersUseCase
import app.qup.reception_management.domain.use_case.GetUserUseCase
import app.qup.reception_management.domain.use_case.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddReceptionViewModel @Inject constructor(
    private val addReceptionUseCase: AddReceptionUseCase,
    private val getGendersUseCase: GetGendersUseCase,
    private val checkRoleEligibilityUseCase: CheckRoleEligibilityUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _addReception: MutableLiveData<ReceptionState> = MutableLiveData()
    val addReception: LiveData<ReceptionState>
        get() = _addReception

    private val _getUser: MutableLiveData<ReceptionState> = MutableLiveData()
    val getUser: LiveData<ReceptionState>
        get() = _getUser

    private val _updateUser: MutableLiveData<ReceptionState> = MutableLiveData()
    val updateUser: LiveData<ReceptionState>
        get() = _updateUser

    private val _genders: MutableLiveData<GetGendersState> = MutableLiveData()
    val genders: LiveData<GetGendersState>
        get() = _genders

    private val _roleEligibility: MutableLiveData<CheckRoleEligibilityState> = MutableLiveData()
    val roleEligibility: LiveData<CheckRoleEligibilityState>
        get() = _roleEligibility

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

    fun checkRoleEligibility(
        mobileNumber: String,
        role: String
    ) {
        checkRoleEligibilityUseCase(mobileNumber, role).onEach {
            _roleEligibility.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getUser(
        mobileNumber: String
    ) {
        getUserUseCase(mobileNumber).onEach {
            _getUser.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun updateUser(
        mobileNumber: String,
        addReceptionRequestDto: AddReceptionRequestDto
    ) {
        updateUserUseCase(mobileNumber, addReceptionRequestDto).onEach {
            _updateUser.postValue(it)
        }.launchIn(viewModelScope)
    }
}