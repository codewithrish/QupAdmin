package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.response.toFacilitySet
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFacilitiesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(FacilitiesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getFacilities().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                FacilitiesState(
                                    isLoading = false,
                                    facilities = it.body()?.map { it1 -> it1.toFacilitySet() }
                                )
                            )
                        } else {
                            send(
                                FacilitiesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(FacilitiesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class FacilitiesState(
    val isLoading: Boolean = false, val facilities: List<FacilitySet>? = null, val error: String? = ""
)