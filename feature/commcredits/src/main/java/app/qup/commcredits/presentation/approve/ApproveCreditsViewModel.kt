package app.qup.commcredits.presentation.approve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.commcredits.data.remote.dto.request.ApproveNotificationCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.ApproveSmsCreditsRequestDto
import app.qup.commcredits.domain.use_case.ApproveNotificationCreditsState
import app.qup.commcredits.domain.use_case.ApproveNotificationCreditsUseCase
import app.qup.commcredits.domain.use_case.ApproveSmsCreditsState
import app.qup.commcredits.domain.use_case.ApproveSmsCreditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ApproveCreditsViewModel @Inject constructor(
    private val approveSmsCreditsUseCase: ApproveSmsCreditsUseCase,
    private val approveNotificationCreditsUseCase: ApproveNotificationCreditsUseCase,
) : ViewModel() {

    private val _approveSmsCredits: MutableLiveData<ApproveSmsCreditsState> = MutableLiveData()
    val approveSmsCredits: LiveData<ApproveSmsCreditsState>
        get() = _approveSmsCredits

    private val _approveNotificationCredits: MutableLiveData<ApproveNotificationCreditsState> = MutableLiveData()
    val approveNotificationCredits: LiveData<ApproveNotificationCreditsState>
        get() = _approveNotificationCredits

    fun approveSmsCredits(
        requestId: String,
        approveSmsCreditsRequestDto: ApproveSmsCreditsRequestDto
    ) {
        approveSmsCreditsUseCase(requestId, approveSmsCreditsRequestDto).onEach {
            _approveSmsCredits.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun approveNotificationCredits(
        requestId: String,
        approveNotificationCreditsRequestDto: ApproveNotificationCreditsRequestDto
    ) {
        approveNotificationCreditsUseCase(requestId, approveNotificationCreditsRequestDto).onEach {
            _approveNotificationCredits.postValue(it)
        }.launchIn(viewModelScope)
    }
}