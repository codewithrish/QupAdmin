package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.general.toNotificationRechargeRequestModel
import app.qup.commcredits.data.remote.dto.request.ApproveNotificationCreditsRequestDto
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApproveNotificationCreditsUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke(
        requestId: String, 
        approveNotificationCreditsRequestDto: ApproveNotificationCreditsRequestDto
    ) = channelFlow {
        send(ApproveNotificationCreditsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.approveNotificationCredits(requestId, approveNotificationCreditsRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                ApproveNotificationCreditsState(
                                    isLoading = false,
                                    notificationRechargeRequestModel = it.body()?.toNotificationRechargeRequestModel()
                                )
                            )
                        } else {
                            send(
                                ApproveNotificationCreditsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(ApproveNotificationCreditsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class ApproveNotificationCreditsState(
    val isLoading: Boolean = false, val notificationRechargeRequestModel: NotificationRechargeRequestModel? = null, val error: String? = ""
)