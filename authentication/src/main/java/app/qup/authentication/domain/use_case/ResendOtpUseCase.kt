package app.qup.authentication.domain.use_case

import app.qup.authentication.data.remote.dto.response.toResendOtpInfo
import app.qup.authentication.domain.model.ResendOtpInfo
import app.qup.authentication.domain.repository.AuthRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        mobileNumber: String
    ) = channelFlow {
        send(ResendOtpState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                authRepository.resendOtp(mobileNumber).also {
                    if (it.isSuccessful) {
                        send(
                            ResendOtpState(
                                isLoading = false,
                                resendOtpInfo = it.body()?.toResendOtpInfo()
                            )
                        )
                    } else {
                        send(
                            ResendOtpState(
                                isLoading = false,
                                error = parseErrorResponse(it.errorBody())
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            send(ResendOtpState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class ResendOtpState(
    val isLoading: Boolean = false,
    val resendOtpInfo: ResendOtpInfo? = null,
    val error: String? = ""
)