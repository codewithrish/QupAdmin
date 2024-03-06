package app.qup.srk_management.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.srk_management.data.remote.dto.general.toSrk
import app.qup.srk_management.data.remote.dto.request.AddSrkRequestDto
import app.qup.srk_management.domain.repository.SrkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val srkRepository: SrkRepository
) {
    operator fun invoke(
        mobileNumber: String, addSrkRequestDto: AddSrkRequestDto
    ) = channelFlow {
        send(SrkState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                srkRepository.updateUser(mobileNumber, addSrkRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SrkState(
                                    isLoading = false,
                                    srk = it.body()?.toSrk()
                                )
                            )
                        } else {
                            send(
                                SrkState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SrkState(isLoading = false, error = e.localizedMessage))
        }
    }
}