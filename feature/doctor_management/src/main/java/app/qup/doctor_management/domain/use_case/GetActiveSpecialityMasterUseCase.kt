package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.response.toSpeciality
import app.qup.doctor_management.domain.model.Speciality
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetActiveSpecialityMasterUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke() = channelFlow {
        send(SpecialityState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getActiveSpecialityMaster().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SpecialityState(
                                    isLoading = false,
                                    specialities = it.body()?.map { it1 -> it1.toSpeciality() }
                                )
                            )
                        } else {
                            send(
                                SpecialityState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SpecialityState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SpecialityState(
    val isLoading: Boolean = false, val specialities: List<Speciality>? = null, val error: String? = ""
)