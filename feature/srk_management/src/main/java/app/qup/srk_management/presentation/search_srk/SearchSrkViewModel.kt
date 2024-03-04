package app.qup.srk_management.presentation.search_srk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.qup.srk_management.data.remote.dto.request.SearchUserRequestDto
import app.qup.srk_management.domain.model.Srk
import app.qup.srk_management.domain.use_case.GetAllSrkUseCase
import app.qup.srk_management.domain.use_case.SearchUserByNameState
import app.qup.srk_management.domain.use_case.SearchUserByNameUseCase
import app.qup.srk_management.domain.use_case.SearchUserByNumberState
import app.qup.srk_management.domain.use_case.SearchUserByNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchSrkViewModel @Inject constructor(
    private val getAllSrkUseCase: GetAllSrkUseCase,
    private val searchUserByNameUseCase: SearchUserByNameUseCase,
    private val searchUserByNumberUseCase: SearchUserByNumberUseCase
) : ViewModel() {
    private lateinit var _srkList: Flow<PagingData<Srk>>
    val srkList: Flow<PagingData<Srk>>
        get() = _srkList

    private val _searchedSrkList: MutableLiveData<SearchUserByNameState> = MutableLiveData()
    val searchedSrkList: LiveData<SearchUserByNameState>
        get() = _searchedSrkList

    private val _searchedSrk: MutableLiveData<SearchUserByNumberState> = MutableLiveData()
    val searchedSrk: LiveData<SearchUserByNumberState>
        get() = _searchedSrk
    fun getAllSrk(
        role: String,
        size: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _srkList = getAllSrkUseCase(role, size).cachedIn(this)
        }
    }

    fun searchUserByName (
        searchUserRequestDto: SearchUserRequestDto
    ) {
        searchUserByNameUseCase(searchUserRequestDto).onEach {
            _searchedSrkList.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun searchUserByNumber(
        role: String,
        mobileNumber: String
    ) {
        searchUserByNumberUseCase(role, mobileNumber).onEach {
            _searchedSrk.postValue(it)
        }.launchIn(viewModelScope)
    }
}