package app.qup.authentication.domain.use_case

import android.util.Base64
import app.qup.authentication.common.PASSWORD
import app.qup.authentication.common.SCOPE
import app.qup.authentication.data.remote.dto.response.toTokens
import app.qup.authentication.domain.model.Tokens
import app.qup.authentication.domain.repository.AuthRepository
import app.qup.network.common.parseErrorResponse
import app.qup.util.common.CLIENT_PASSWORD
import app.qup.util.common.CLIENT_USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        grantType: String = PASSWORD,
        scope: String = SCOPE,
        username: String,
        password: String
    ) = channelFlow {
        send(VerifyOtpState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                val credentials = "$CLIENT_USERNAME:$CLIENT_PASSWORD"
                val encodedString = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                authRepository.verifyOtp(encodedString, grantType, scope, username, password).also {
                    if (it.isSuccessful) {
                        send(VerifyOtpState(isLoading = false, tokens = it.body()?.toTokens()))
                    } else {
                        send(VerifyOtpState(isLoading = false, error = parseErrorResponse(it.errorBody())))
                    }
                }
            }
        } catch (e: Exception) {
            send(VerifyOtpState(isLoading = false, error = e.localizedMessage))
        }
    }
}
data class VerifyOtpState(
    val isLoading: Boolean = false,
    val tokens: Tokens? = null,
    val error: String? = ""
)