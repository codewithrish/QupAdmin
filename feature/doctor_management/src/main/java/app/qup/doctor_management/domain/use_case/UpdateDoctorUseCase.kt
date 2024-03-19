package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.request.DoctorRequestDto
import app.qup.doctor_management.data.remote.dto.response.toDoctor
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDoctorUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke(
        id: String,
        doctorRequestDto: DoctorRequestDto
    ) = channelFlow {
        send(UpdateDoctorState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.updateDoctorById(id, doctorRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                UpdateDoctorState(
                                    isLoading = false,
                                    doctor = it.body()?.toDoctor()
                                )
                            )
                        } else {
                            send(
                                UpdateDoctorState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(UpdateDoctorState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class UpdateDoctorState(
    val isLoading: Boolean = false, val doctor: Doctor? = null, val error: String? = ""
)