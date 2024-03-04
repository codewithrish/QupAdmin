package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.general.toDoctor
import app.qup.doctor_management.domain.model.Doctor
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchDoctorByNumberUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke(
        mobileNumber: String
    ) = channelFlow {
        send(SearchDoctorByNumberState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.searchDoctorByNumber(mobileNumber).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SearchDoctorByNumberState(
                                    isLoading = false,
                                    doctor = it.body()?.toDoctor()
                                )
                            )
                        } else {
                            send(
                                SearchDoctorByNumberState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SearchDoctorByNumberState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SearchDoctorByNumberState(
    val isLoading: Boolean = false, val doctor: Doctor? = null, val error: String? = ""
)