package app.qup.commcredits.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.commcredits.data.remote.dto.request.MarkPaymentDoneRequestDto
import app.qup.commcredits.domain.use_case.MarkNotificationPaymentPaidState
import app.qup.commcredits.domain.use_case.MarkNotificationPaymentPaidUseCase
import app.qup.commcredits.domain.use_case.MarkSmsPaymentPaidState
import app.qup.commcredits.domain.use_case.MarkSmsPaymentPaidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val markSmsPaymentPaidUseCase: MarkSmsPaymentPaidUseCase,
    private val markNotificationPaymentPaidUseCase: MarkNotificationPaymentPaidUseCase,
) : ViewModel() {
    private val _smsPayment: MutableLiveData<MarkSmsPaymentPaidState> = MutableLiveData()
    val smsPayment: LiveData<MarkSmsPaymentPaidState>
        get() = _smsPayment

    private val _notificationPayment: MutableLiveData<MarkNotificationPaymentPaidState> = MutableLiveData()
    val notificationPayment: LiveData<MarkNotificationPaymentPaidState>
        get() = _notificationPayment

    fun makeSmsPaymentPaid(
        requestId: String,
        markPaymentDoneRequestDto: MarkPaymentDoneRequestDto
    ) {
        markSmsPaymentPaidUseCase(requestId, markPaymentDoneRequestDto).onEach {
            _smsPayment.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun makeNotificationPaymentPaid(
        requestId: String,
        markPaymentDoneRequestDto: MarkPaymentDoneRequestDto
    ) {
        markNotificationPaymentPaidUseCase(requestId, markPaymentDoneRequestDto).onEach {
            _notificationPayment.postValue(it)
        }.launchIn(viewModelScope)
    }
}