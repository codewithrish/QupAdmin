package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.response.toDoctor
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetDoctorByIdUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke(
        id: String
    ) = channelFlow {
        send(GetDoctorByIdState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getDoctorById(id).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetDoctorByIdState(
                                    isLoading = false,
                                    doctor = it.body()?.toDoctor()
                                )
                            )
                        } else {
                            send(
                                GetDoctorByIdState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetDoctorByIdState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetDoctorByIdState(
    val isLoading: Boolean = false, val doctor: Doctor? = null, val error: String? = ""
)