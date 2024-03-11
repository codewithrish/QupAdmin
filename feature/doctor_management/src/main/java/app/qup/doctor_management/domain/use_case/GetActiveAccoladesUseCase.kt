package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.response.toAccoladeSet
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetActiveAccoladesUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke() = channelFlow {
        send(AccoladesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getActiveAccolades().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                AccoladesState(
                                    isLoading = false,
                                    accolades = it.body()?.map { it1 -> it1.toAccoladeSet() }
                                )
                            )
                        } else {
                            send(
                                AccoladesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(AccoladesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class AccoladesState(
    val isLoading: Boolean = false, val accolades: List<AccoladesSet>? = null, val error: String? = ""
)