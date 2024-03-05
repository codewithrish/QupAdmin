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

class AddDoctorUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke(
        doctorRequestDto: DoctorRequestDto
    ) = channelFlow {
        send(AddDoctorState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.addDoctor(doctorRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                AddDoctorState(
                                    isLoading = false,
                                    doctor = it.body()?.toDoctor()
                                )
                            )
                        } else {
                            send(
                                AddDoctorState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(AddDoctorState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class AddDoctorState(
    val isLoading: Boolean = false, val doctor: Doctor? = null, val error: String? = ""
)