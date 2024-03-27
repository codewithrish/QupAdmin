package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.data.remote.dto.response.toTopUpSms
import app.qup.commcredits.domain.model.TopUpSms
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopUpSmsUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke(
        topUpSmsRequestDto: TopUpSmsRequestDto
    ) = channelFlow {
        send(TopUpSmsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.topUpSms(topUpSmsRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                TopUpSmsState(
                                    isLoading = false,
                                    topUpSms = it.body()?.toTopUpSms()
                                )
                            )
                        } else {
                            send(
                                TopUpSmsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(TopUpSmsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class TopUpSmsState(
    val isLoading: Boolean = false, val topUpSms: TopUpSms? = null, val error: String? = ""
)