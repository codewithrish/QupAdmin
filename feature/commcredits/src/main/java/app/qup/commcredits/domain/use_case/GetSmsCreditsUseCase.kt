package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.response.toSmsCredits
import app.qup.commcredits.domain.model.SmsCredits
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSmsCreditsUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke() = channelFlow {
        send(GetSmsCreditsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.getSmsCredits().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetSmsCreditsState(
                                    isLoading = false,
                                    smsCredits = it.body()?.toSmsCredits()
                                )
                            )
                        } else {
                            send(
                                GetSmsCreditsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetSmsCreditsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetSmsCreditsState(
    val isLoading: Boolean = false, val smsCredits: SmsCredits? = null, val error: String? = ""
)