package app.qup.reception_management.presentation.search_reception

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.qup.reception_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.domain.use_case.GetAllReceptionsUseCase
import app.qup.reception_management.domain.use_case.SearchUserByNameState
import app.qup.reception_management.domain.use_case.SearchUserByNameUseCase
import app.qup.reception_management.domain.use_case.SearchUserByNumberState
import app.qup.reception_management.domain.use_case.SearchUserByNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchReceptionViewModel @Inject constructor(
    private val getAllReceptionsUseCase: GetAllReceptionsUseCase,
    private val searchUserByNameUseCase: SearchUserByNameUseCase,
    private val searchUserByNumberUseCase: SearchUserByNumberUseCase
) : ViewModel() {
    private lateinit var _receptions: Flow<PagingData<Reception>>
    val receptions: Flow<PagingData<Reception>>
        get() = _receptions

    private val _searchedReceptions: MutableLiveData<SearchUserByNameState> = MutableLiveData()
    val searchedReceptions: LiveData<SearchUserByNameState>
        get() = _searchedReceptions

    private val _searchedReception: MutableLiveData<SearchUserByNumberState> = MutableLiveData()
    val searchedReception: LiveData<SearchUserByNumberState>
        get() = _searchedReception

    fun getAllReceptions(
        role: String,
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _receptions = getAllReceptionsUseCase(role, size).cachedIn(this)
        }
    }

    fun searchUserByName (
        searchUserRequestDto: SearchUserRequestDto
    ) {
        searchUserByNameUseCase(searchUserRequestDto).onEach {
            _searchedReceptions.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun searchUserByNumber(
        role: String,
        mobileNumber: String
    ) {
        searchUserByNumberUseCase(role, mobileNumber).onEach {
            _searchedReception.postValue(it)
        }.launchIn(viewModelScope)
    }
}