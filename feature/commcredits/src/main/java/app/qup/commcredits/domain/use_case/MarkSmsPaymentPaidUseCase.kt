package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.request.MarkPaymentDoneRequestDto
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkSmsPaymentPaidUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke(
        requestId: String, 
        markPaymentDoneRequestDto: MarkPaymentDoneRequestDto
    ) = channelFlow {
        send(MarkSmsPaymentPaidState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.markSmsPaymentPaid(requestId, markPaymentDoneRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                MarkSmsPaymentPaidState(
                                    isLoading = false,
                                    success = true
                                )
                            )
                        } else {
                            send(
                                MarkSmsPaymentPaidState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(MarkSmsPaymentPaidState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class MarkSmsPaymentPaidState(
    val isLoading: Boolean = false, val success: Boolean? = null, val error: String? = ""
)