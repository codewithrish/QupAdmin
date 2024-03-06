package app.qup.srk_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.srk_management.data.remote.dto.general.toSrk
import app.qup.srk_management.domain.model.Srk
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CheckRoleEligibilityUseCase @Inject constructor(
    private val srkRepository: SrkRepository
) {
    operator fun invoke(
        mobileNumber: String, 
        role: String
    ) = channelFlow {
        send(CheckRoleEligibilityState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                srkRepository.checkRoleEligibility(mobileNumber, role).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(CheckRoleEligibilityState(isLoading = false, srk = it.body()?.toSrk()))
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
    val srk: Srk? = null,
    val error: String? = "",
    val errorCode: Int = 0
)