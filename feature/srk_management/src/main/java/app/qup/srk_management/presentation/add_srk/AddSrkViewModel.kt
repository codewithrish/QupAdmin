package app.qup.srk_management.presentation.add_srk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.srk_management.data.remote.dto.request.AddSrkRequestDto
import app.qup.srk_management.domain.use_case.SrkState
import app.qup.srk_management.domain.use_case.AddSrkUseCase
import app.qup.srk_management.domain.use_case.CheckRoleEligibilityState
import app.qup.srk_management.domain.use_case.CheckRoleEligibilityUseCase
import app.qup.srk_management.domain.use_case.GetGendersState
import app.qup.srk_management.domain.use_case.GetGendersUseCase
import app.qup.srk_management.domain.use_case.GetUserUseCase
import app.qup.srk_management.domain.use_case.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddSrkViewModel @Inject constructor(
    private val addSrkUseCase: AddSrkUseCase,
    private val getGendersUseCase: GetGendersUseCase,
    private val checkRoleEligibilityUseCase: CheckRoleEligibilityUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _addSrk: MutableLiveData<SrkState> = MutableLiveData()
    val addSrk: LiveData<SrkState>
        get() = _addSrk

    private val _getUser: MutableLiveData<SrkState> = MutableLiveData()
    val getUser: LiveData<SrkState>
        get() = _getUser

    private val _updateUser: MutableLiveData<SrkState> = MutableLiveData()
    val updateUser: LiveData<SrkState>
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
        addSrkRequestDto: AddSrkRequestDto
    ) {
        updateUserUseCase(mobileNumber, addSrkRequestDto).onEach {
            _updateUser.postValue(it)
        }.launchIn(viewModelScope)
    }
}