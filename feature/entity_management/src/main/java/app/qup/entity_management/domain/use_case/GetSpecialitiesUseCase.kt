package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.response.toEntitySpecialitySet
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSpecialitiesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(SpecialitiesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getSpecialities().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                SpecialitiesState(
                                    isLoading = false,
                                    entitySpecialities = it.body()?.map { it1 -> it1.toEntitySpecialitySet() }
                                )
                            )
                        } else {
                            send(
                                SpecialitiesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(SpecialitiesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class SpecialitiesState(
    val isLoading: Boolean = false, val entitySpecialities: List<EntitySpecialitySet>? = null, val error: String? = ""
)