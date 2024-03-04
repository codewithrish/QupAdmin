package app.qup.doctor_management.presentation.search_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.use_case.GetAllDoctorsUseCase
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberState
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberUseCase
import app.qup.doctor_management.domain.use_case.SearchDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDoctorViewModel @Inject constructor(
    private val getAllDoctorsUseCase: GetAllDoctorsUseCase,
    private val searchDoctorsUseCase: SearchDoctorsUseCase,
    private val searchDoctorByNumberUseCase: SearchDoctorByNumberUseCase
) : ViewModel() {
    private lateinit var _doctors: Flow<PagingData<Doctor>>
    val doctors: Flow<PagingData<Doctor>>
        get() = _doctors

    private lateinit var _searchDoctors: Flow<PagingData<Doctor>>
    val searchDoctors: Flow<PagingData<Doctor>>
        get() = _searchDoctors

//    private lateinit var _searchDoctorByNumber: Flow<PagingData<Doctor>>
//    val searchDoctorByNumber: Flow<PagingData<Doctor>>
//        get() = _searchDoctorByNumber

    private val _searchDoctorByNumber: MutableLiveData<SearchDoctorByNumberState> = MutableLiveData()
    val searchDoctorByNumber: LiveData<SearchDoctorByNumberState>
        get() = _searchDoctorByNumber

    fun getAllDoctors(
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _doctors = getAllDoctorsUseCase(size).cachedIn(this)
        }
    }

    fun searchDoctorByName(
        doctorName: String,
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchDoctors = searchDoctorsUseCase(doctorName, size).cachedIn(this)
        }
    }

    fun searchDoctorByNumber(
        mobileNumber: String
    ) {
        searchDoctorByNumberUseCase(mobileNumber).onEach {
            _searchDoctorByNumber.postValue(it)
        }.launchIn(viewModelScope)
//        viewModelScope.launch {
//            searchDoctorByNumberUseCase(mobileNumber).onEach {
//                it.doctor?.let { doctor ->
//                    _searchDoctorByNumber = flow<PagingData<Doctor>> { PagingData.from(listOf(doctor)) }.cachedIn(this)
//                }
//            }
//        }
    }
}