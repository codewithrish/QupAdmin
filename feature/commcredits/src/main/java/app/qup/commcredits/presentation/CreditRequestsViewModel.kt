package app.qup.commcredits.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.domain.use_case.GetNotificationCreditsState
import app.qup.commcredits.domain.use_case.GetNotificationCreditsUseCase
import app.qup.commcredits.domain.use_case.GetSmsCreditsState
import app.qup.commcredits.domain.use_case.GetSmsCreditsUseCase
import app.qup.commcredits.domain.use_case.TopUpSmsState
import app.qup.commcredits.domain.use_case.TopUpSmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CreditRequestsViewModel @Inject constructor(
    private val getSmsCreditsUseCase: GetSmsCreditsUseCase,
    private val getNotificationCreditsUseCase: GetNotificationCreditsUseCase,
    private val topUpSmsUseCase: TopUpSmsUseCase,
) : ViewModel() {

    private val _smsCreditRequests: MutableLiveData<GetSmsCreditsState> = MutableLiveData()
    val smsCreditRequests: LiveData<GetSmsCreditsState>
        get() = _smsCreditRequests

    private val _notificationCreditRequests: MutableLiveData<GetNotificationCreditsState> = MutableLiveData()
    val notificationCreditRequests: LiveData<GetNotificationCreditsState>
        get() = _notificationCreditRequests

    private val _topPupSmsCredits: MutableLiveData<TopUpSmsState> = MutableLiveData()
    val topPupSmsCredits: LiveData<TopUpSmsState>
        get() = _topPupSmsCredits




    fun getSmsCreditRequests() {
        getSmsCreditsUseCase().onEach {
            _smsCreditRequests.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun getNotificationCreditRequests() {
        getNotificationCreditsUseCase().onEach {
            _notificationCreditRequests.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun topPupSmsCredits(
        topUpSmsRequestDto: TopUpSmsRequestDto
    ) {
        topUpSmsUseCase(topUpSmsRequestDto).onEach {
            _topPupSmsCredits.postValue(it)
        }.launchIn(viewModelScope)
    }
}