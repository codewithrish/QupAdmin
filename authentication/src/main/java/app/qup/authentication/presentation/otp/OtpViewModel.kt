package app.qup.authentication.presentation.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.authentication.domain.use_case.ResendOtpState
import app.qup.authentication.domain.use_case.ResendOtpUseCase
import app.qup.authentication.domain.use_case.ValidateOtpFormUseCase
import app.qup.authentication.domain.use_case.VerifyOtpState
import app.qup.authentication.domain.use_case.VerifyOtpUseCase
import app.qup.util.common.JWT_ACCESS_TOKEN_PREF
import app.qup.util.common.JWT_REFRESH_TOKEN_PREF
import app.qup.util.common.QupSharedPrefManager
import app.qup.util.common.USER_MOBILE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val resendOtpUseCase: ResendOtpUseCase,
    private val validateOtpFormUseCase: ValidateOtpFormUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,

    private val qupSharedPrefManager: QupSharedPrefManager
): ViewModel() {
    private var _validForm: MutableLiveData<Boolean> = MutableLiveData(false)
    val validForm: LiveData<Boolean>
        get() = _validForm
    private var _verifyOtp: MutableLiveData<VerifyOtpState> = MutableLiveData()
    val verifyOtp: LiveData<VerifyOtpState>
        get() = _verifyOtp
    private var _resendOtp: MutableLiveData<ResendOtpState> = MutableLiveData()
    val resendOtp: LiveData<ResendOtpState>
        get() = _resendOtp

    fun validateOtpForm(
        otp: String
    ) {
        _validForm.postValue(validateOtpFormUseCase(otp))
    }
    fun verifyOtp(
        username: String,
        password: String
    ) {
        verifyOtpUseCase(username = username, password = password).onEach {
            _verifyOtp.postValue(it)
        }.launchIn(viewModelScope)
    }
    fun resendOtp(
        mobileNumber: String
    ) {
        resendOtpUseCase(mobileNumber).onEach {
            _resendOtp.postValue(it)
        }.launchIn(viewModelScope)
    }
    fun saveAccessToken(accessToken: String) {
        qupSharedPrefManager.save(JWT_ACCESS_TOKEN_PREF, accessToken)
    }
    fun saveRefreshToken(refreshToken: String) {
        qupSharedPrefManager.save(JWT_REFRESH_TOKEN_PREF, refreshToken)
    }
    fun saveUserMobileNumber(mobileNumber: String) {
        qupSharedPrefManager.save(USER_MOBILE_NUMBER, mobileNumber)
    }
}