package app.qup.authentication.domain.use_case

import android.util.Log
import app.qup.authentication.data.remote.dto.request.SignInRequestDto
import app.qup.authentication.data.remote.dto.response.toSignInInfo
import app.qup.authentication.domain.model.SignInInfo
import app.qup.authentication.domain.repository.AuthRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(
        mobileNumber: String,
        signInRequestDto: SignInRequestDto
    ) = channelFlow {
        send(SignInState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                authRepository.signIn(mobileNumber, signInRequestDto).also {
                    if (it.isSuccessful) {
                        send(SignInState(isLoading = false, signInInfo = it.body()?.toSignInInfo()))
                    } else {
                        send(
                            SignInState(
                                isLoading = false,
                                error = parseErrorResponse(it.errorBody()),
                                errorCode = it.code()
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "login: ${e.printStackTrace()}")
            send(SignInState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SignInState(
    val isLoading: Boolean = false,
    val signInInfo: SignInInfo? = null,
    val error: String? = "",
    val errorCode: Int = 0
)