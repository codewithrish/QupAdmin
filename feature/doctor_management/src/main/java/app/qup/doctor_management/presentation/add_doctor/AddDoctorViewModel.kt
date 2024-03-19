package app.qup.doctor_management.presentation.add_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.domain.use_case.AccoladesState
import app.qup.doctor_management.domain.use_case.AddDoctorState
import app.qup.doctor_management.domain.use_case.AddDoctorUseCase
import app.qup.doctor_management.domain.use_case.DegreeState
import app.qup.doctor_management.domain.use_case.GetActiveAccoladesUseCase
import app.qup.doctor_management.domain.use_case.GetActiveDegreeMasterUseCase
import app.qup.doctor_management.domain.use_case.GetActiveSpecialityCategoryMasterUseCase
import app.qup.doctor_management.domain.use_case.GetActiveSpecialityMasterUseCase
import app.qup.doctor_management.domain.use_case.GetDoctorByIdState
import app.qup.doctor_management.domain.use_case.GetDoctorByIdUseCase
import app.qup.doctor_management.domain.use_case.GetGendersState
import app.qup.doctor_management.domain.use_case.GetGendersUseCase
import app.qup.doctor_management.domain.use_case.GetUserByNumberState
import app.qup.doctor_management.domain.use_case.GetUserByNumberUseCase
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberState
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberUseCase
import app.qup.doctor_management.domain.use_case.SpecialityCategoryState
import app.qup.doctor_management.domain.use_case.SpecialityState
import app.qup.doctor_management.domain.use_case.UpdateDoctorState
import app.qup.doctor_management.domain.use_case.UpdateDoctorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AddDoctorViewModel @Inject constructor(
    private val addDoctorUseCase: AddDoctorUseCase,
    private val updateDoctorUseCase: UpdateDoctorUseCase,
    private val getGendersUseCase: GetGendersUseCase,
    private val getActiveAccoladesUseCase: GetActiveAccoladesUseCase,
    private val getActiveDegreeMasterUseCase: GetActiveDegreeMasterUseCase,
    private val getActiveSpecialityCategoryMasterUseCase: GetActiveSpecialityCategoryMasterUseCase,
    private val getActiveSpecialityMasterUseCase: GetActiveSpecialityMasterUseCase,
    private val searchDoctorByNumberUseCase: SearchDoctorByNumberUseCase,
    private val getDoctorByIdUseCase: GetDoctorByIdUseCase,
    private val getUserByNumberUseCase: GetUserByNumberUseCase,
) : ViewModel() {

    val stepNumber: MutableLiveData<Int> = MutableLiveData(1)

    private val _addDoctor: MutableLiveData<AddDoctorState> = MutableLiveData()
    val addDoctor: LiveData<AddDoctorState>
        get() = _addDoctor

    private val _updateDoctor: MutableLiveData<UpdateDoctorState> = MutableLiveData()
    val updateDoctor: LiveData<UpdateDoctorState>
        get() = _updateDoctor

    private val _getDoctorById: MutableLiveData<GetDoctorByIdState> = MutableLiveData()
    val getDoctorById: LiveData<GetDoctorByIdState>
        get() = _getDoctorById

    private val _userInfo: MutableLiveData<GetUserByNumberState> = MutableLiveData()
    val userInfo: LiveData<GetUserByNumberState>
        get() = _userInfo

    private val _genders: MutableLiveData<GetGendersState> = MutableLiveData()
    val genders: LiveData<GetGendersState>
        get() = _genders

    private val _accolades: MutableLiveData<AccoladesState> = MutableLiveData()
    val accolades: LiveData<AccoladesState>
        get() = _accolades

    private val _degrees: MutableLiveData<DegreeState> = MutableLiveData()
    val degrees: LiveData<DegreeState>
        get() = _degrees

    private val _specialityCategories: MutableLiveData<SpecialityCategoryState> = MutableLiveData()
    val specialityCategories: LiveData<SpecialityCategoryState>
        get() = _specialityCategories

    private val _specialities: MutableLiveData<SpecialityState> = MutableLiveData()
    val specialities: LiveData<SpecialityState>
        get() = _specialities
    private val _searchDoctorByName: MutableLiveData<SearchDoctorByNumberState> = MutableLiveData()
    val searchDoctorByName: LiveData<SearchDoctorByNumberState>
        get() = _searchDoctorByName

    init {
        getGenders()
    }

    fun addDoctor(
        doctorRequestDto: DoctorRequestDto
    ) {
        addDoctorUseCase(doctorRequestDto).onEach {
            _addDoctor.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun updateDoctor(
        id: String,
        doctorRequestDto: DoctorRequestDto
    ) {
        updateDoctorUseCase(id, doctorRequestDto).onEach {
            _updateDoctor.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getDoctorById(
        id: String
    ) {
        getDoctorByIdUseCase(id).onEach {
            _getDoctorById.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getUserByNumber(
        mobileNumber: String
    ) {
        getUserByNumberUseCase(mobileNumber).onEach {
            _userInfo.postValue(it)
        }.launchIn(viewModelScope)
    }

    private fun getGenders() {
        getGendersUseCase().onEach {
            _genders.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getAccolades() {
        getActiveAccoladesUseCase().onEach {
            _accolades.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getDegrees() {
        getActiveDegreeMasterUseCase().onEach {
            _degrees.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getSpecialityCategories() {
        getActiveSpecialityCategoryMasterUseCase().onEach {
            _specialityCategories.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getSpecialities() {
        getActiveSpecialityMasterUseCase().onEach {
            _specialities.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun searchDoctorByName(
        mobileNumber: String
    ) {
        searchDoctorByNumberUseCase(mobileNumber).onEach {
            _searchDoctorByName.postValue(it)
        }.launchIn(viewModelScope)
    }
}