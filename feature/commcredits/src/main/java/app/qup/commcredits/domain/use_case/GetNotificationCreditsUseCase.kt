package app.qup.commcredits.domain.use_case

import app.qup.commcredits.data.remote.dto.general.toNotificationRechargeRequestModel
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel
import app.qup.commcredits.domain.repository.CommRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNotificationCreditsUseCase @Inject constructor(
    private val commRepository: CommRepository
) {
    operator fun invoke() = channelFlow {
        send(GetNotificationCreditsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                commRepository.getNotificationCredits().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetNotificationCreditsState(
                                    isLoading = false,
                                    notificationRechargeRequestModels = it.body()?.notificationRechargeRequestList?.map { it1 -> it1.toNotificationRechargeRequestModel() }
                                )
                            )
                        } else {
                            send(
                                GetNotificationCreditsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetNotificationCreditsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetNotificationCreditsState(
    val isLoading: Boolean = false, val notificationRechargeRequestModels: List<NotificationRechargeRequestModel>? = null, val error: String? = ""
)