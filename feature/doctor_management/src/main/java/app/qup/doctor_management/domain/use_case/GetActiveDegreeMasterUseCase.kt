package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.response.toDegree
import app.qup.doctor_management.domain.model.Degree
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetActiveDegreeMasterUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke() = channelFlow {
        send(DegreeState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getActiveDegreeMaster().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                DegreeState(
                                    isLoading = false,
                                    degrees = it.body()?.map { it1 -> it1.toDegree() }
                                )
                            )
                        } else {
                            send(
                                DegreeState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(DegreeState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class DegreeState(
    val isLoading: Boolean = false, val degrees: List<Degree>? = null, val error: String? = ""
)