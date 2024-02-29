package app.qup.authentication.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.domain.use_case.SignInState
import app.qup.authentication.domain.use_case.SignInUseCase
import app.qup.authentication.domain.use_case.ValidateLoginFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateLoginFormUseCase: ValidateLoginFormUseCase,
    private val signInUseCase: SignInUseCase
): ViewModel() {
    private var _validForm: MutableLiveData<Boolean> = MutableLiveData(false)
    val validForm: LiveData<Boolean>
        get() = _validForm

    private var _login: MutableSharedFlow<SignInState> = MutableSharedFlow()
    val login: SharedFlow<SignInState>
        get() = _login

    fun validateLoginForm(
        mobileNumber: String,
        cbChecked: Boolean
    ) {
        _validForm.postValue(validateLoginFormUseCase(mobileNumber, cbChecked))
    }
    fun login(
        mobileNumber: String,
        signInRequestDto: SignInRequestDto
    ) {
        signInUseCase(mobileNumber, signInRequestDto).onEach {
            _login.emit(it)
            //  _login.postValue(it)
        }.launchIn(viewModelScope)
    }
}