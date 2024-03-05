package app.qup.doctor_management.presentation.search_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.qup.doctor_management.domain.model.DoctorR
import app.qup.doctor_management.domain.use_case.GetAllDoctorsUseCase
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberState
import app.qup.doctor_management.domain.use_case.SearchDoctorByNumberUseCase
import app.qup.doctor_management.domain.use_case.SearchDoctorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    private lateinit var _doctors: Flow<PagingData<DoctorR>>
    val doctors: Flow<PagingData<DoctorR>>
        get() = _doctors

    private lateinit var _searchDoctors: Flow<PagingData<DoctorR>>
    val searchDoctors: Flow<PagingData<DoctorR>>
        get() = _searchDoctors

//    private lateinit var _searchDoctorByNumber: Flow<PagingData<DoctorR>>
//    val searchDoctorByNumber: Flow<PagingData<DoctorR>>
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
//                it.doctorR?.let { doctorR ->
//                    _searchDoctorByNumber = flow<PagingData<DoctorR>> { PagingData.from(listOf(doctorR)) }.cachedIn(this)
//                }
//            }
//        }
    }
}