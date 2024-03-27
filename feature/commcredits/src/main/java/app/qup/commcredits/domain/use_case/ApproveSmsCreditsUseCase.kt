package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.general.toSmsRechargeRequestModel
import app.qup.commcredits.data.remote.dto.request.ApproveSmsCreditsRequestDto
import app.qup.commcredits.domain.model.SmsRechargeRequestModel
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApproveSmsCreditsUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke(
        requestId: String, 
        approveSmsCreditsRequestDto: ApproveSmsCreditsRequestDto
    ) = channelFlow {
        send(ApproveSmsCreditsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.approveSmsCredits(requestId, approveSmsCreditsRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                ApproveSmsCreditsState(
                                    isLoading = false,
                                    smsRechargeRequestModel = it.body()?.toSmsRechargeRequestModel()
                                )
                            )
                        } else {
                            send(
                                ApproveSmsCreditsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(ApproveSmsCreditsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class ApproveSmsCreditsState(
    val isLoading: Boolean = false, val smsRechargeRequestModel: SmsRechargeRequestModel? = null, val error: String? = ""
)