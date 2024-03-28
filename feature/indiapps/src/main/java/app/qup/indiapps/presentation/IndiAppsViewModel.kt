package app.qup.indiapps.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.indiapps.domain.use_case.GetIndiAppsState
import app.qup.indiapps.domain.use_case.GetIndiAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class IndiAppsViewModel @Inject constructor(
    private val getIndiAppsUseCase: GetIndiAppsUseCase
) : ViewModel() {
    private val _indiApps: MutableLiveData<GetIndiAppsState> = MutableLiveData()
    val indiApps: LiveData<GetIndiAppsState>
        get() = _indiApps

    fun getIndiApps() {
        getIndiAppsUseCase().onEach {
            _indiApps.postValue(it)
        }.launchIn(viewModelScope)
    }
}