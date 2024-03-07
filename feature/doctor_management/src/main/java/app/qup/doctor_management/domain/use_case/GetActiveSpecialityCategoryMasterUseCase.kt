package app.qup.doctor_management.domain.use_case

import app.qup.doctor_management.data.remote.dto.response.toSpecialityCategory
import app.qup.doctor_management.domain.model.SpecialityCategory
import app.qup.doctor_management.domain.repository.DoctorRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetActiveSpecialityCategoryMasterUseCase @Inject constructor(
    private val doctorRepository: DoctorRepository
) {
    operator fun invoke() = channelFlow {
        send(SpecialityCategoryState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                doctorRepository.getActiveSpecialityCategoryMaster().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SpecialityCategoryState(
                                    isLoading = false,
                                    specialityCategories = it.body()?.map { it1 -> it1.toSpecialityCategory() }
                                )
                            )
                        } else {
                            send(
                                SpecialityCategoryState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SpecialityCategoryState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SpecialityCategoryState(
    val isLoading: Boolean = false, val specialityCategories: List<SpecialityCategory>? = null, val error: String? = ""
)