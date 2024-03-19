package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.general.toUser
import app.qup.doctor_management.domain.model.User
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserByNumberUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke(
        mobileNumber: String
    ) = channelFlow {
        send(GetUserByNumberState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getUserInfo(mobileNumber).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetUserByNumberState(
                                    isLoading = false,
                                    user = it.body()?.toUser()
                                )
                            )
                        } else {
                            send(
                                GetUserByNumberState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetUserByNumberState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetUserByNumberState(
    val isLoading: Boolean = false, val user: User? = null, val error: String? = ""
)