package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.response.toEntityType
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetEntityTypesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(EntityTypesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getEntityTypes().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                EntityTypesState(
                                    isLoading = false,
                                    entityTypes = it.body()?.map { it1 -> it1.toEntityType() }
                                )
                            )
                        } else {
                            send(
                                EntityTypesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(EntityTypesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class EntityTypesState(
    val isLoading: Boolean = false, val entityTypes: List<EntityType>? = null, val error: String? = ""
)