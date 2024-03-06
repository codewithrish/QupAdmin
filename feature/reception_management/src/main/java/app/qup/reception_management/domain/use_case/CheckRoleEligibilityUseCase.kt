package app.qup.reception_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.reception_management.data.remote.dto.general.toReception
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CheckRoleEligibilityUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    operator fun invoke(
        mobileNumber: String, 
        role: String
    ) = channelFlow {
        send(CheckRoleEligibilityState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                receptionRepository.checkRoleEligibility(mobileNumber, role).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(CheckRoleEligibilityState(isLoading = false, reception = it.body()?.toReception()))
                        } else {
                            send(
                                CheckRoleEligibilityState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody()), errorCode = it.code()
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(CheckRoleEligibilityState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class CheckRoleEligibilityState(
    val isLoading: Boolean = false,
    val reception: Reception? = null,
    val error: String? = "",
    val errorCode: Int = 0
)