package app.qup.reception_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.reception_management.data.remote.dto.general.toReception
import app.qup.reception_management.data.remote.dto.request.AddReceptionRequestDto
import app.qup.reception_management.domain.repository.ReceptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val receptionRepository: ReceptionRepository
) {
    operator fun invoke(
        mobileNumber: String,addReceptionRequestDto: AddReceptionRequestDto
    ) = channelFlow {
        send(ReceptionState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                receptionRepository.updateUser(mobileNumber, addReceptionRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(ReceptionState(isLoading = false, reception = it.body()?.toReception()))
                        } else {
                            send(
                                ReceptionState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(ReceptionState(isLoading = false, error = e.localizedMessage))
        }
    }
}