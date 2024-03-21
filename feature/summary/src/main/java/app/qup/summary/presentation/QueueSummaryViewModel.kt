package app.qup.summary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.domain.use_case.CustomOpdStatusValuesState
import app.qup.summary.domain.use_case.CustomQueueSummaryState
import app.qup.summary.domain.use_case.GetCustomOpdStatusValuesUseCase
import app.qup.summary.domain.use_case.GetCustomQueueSummaryUseCase
import app.qup.summary.domain.use_case.GetOpdStatusValuesUseCase
import app.qup.summary.domain.use_case.GetQueueSummaryUseCase
import app.qup.summary.domain.use_case.OpdStatusValuesState
import app.qup.summary.domain.use_case.QueueSummaryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QueueSummaryViewModel @Inject constructor(
    private val getQueueSummaryUseCase: GetQueueSummaryUseCase,
    private val getCustomQueueSummaryUseCase: GetCustomQueueSummaryUseCase,
    private val getOpdStatusValuesUseCase: GetOpdStatusValuesUseCase,
    private val getCustomOpdStatusValuesUseCase: GetCustomOpdStatusValuesUseCase,
) : ViewModel() {
    private val _queueSummary: MutableLiveData<QueueSummaryState> = MutableLiveData()
    val queueSummary: LiveData<QueueSummaryState>
        get() = _queueSummary

    private val _customQueueSummary: MutableLiveData<CustomQueueSummaryState> = MutableLiveData()
    val customQueueSummary: LiveData<CustomQueueSummaryState>
        get() = _customQueueSummary

    private val _opdStatusValues: MutableLiveData<OpdStatusValuesState> = MutableLiveData()
    val opdStatusValues: LiveData<OpdStatusValuesState>
        get() = _opdStatusValues

    private val _customOpdStatusValues: MutableLiveData<CustomOpdStatusValuesState> = MutableLiveData()
    val customOpdStatusValues: LiveData<CustomOpdStatusValuesState>
        get() = _customOpdStatusValues

    fun getQueueSummary(
        summaryRequestDto: SummaryRequestDto
    ) {
        getQueueSummaryUseCase(summaryRequestDto).onEach {
            _queueSummary.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getCustomQueueSummary(
        summaryRequestDto: SummaryRequestDto
    ) {
        getCustomQueueSummaryUseCase(summaryRequestDto).onEach {
            _customQueueSummary.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getOpdStatusValues() {
        getOpdStatusValuesUseCase().onEach {
            _opdStatusValues.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getCustomOpdStatusValues() {
        getCustomOpdStatusValuesUseCase().onEach {
            _customOpdStatusValues.postValue(it)
        }.launchIn(viewModelScope)
    }
}