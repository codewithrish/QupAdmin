package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.request.EntityRequestDto
import app.qup.entity_management.data.remote.dto.response.toEntity
import app.qup.entity_management.domain.model.Entity
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateEntityByIdUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
        id: String,
        entityRequestDto: EntityRequestDto
    ) = channelFlow {
        send(UpdateEntityByIdState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.updateEntityById(id, entityRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                UpdateEntityByIdState(
                                    isLoading = false,
                                    entity = it.body()?.toEntity()
                                )
                            )
                        } else {
                            send(
                                UpdateEntityByIdState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(UpdateEntityByIdState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class UpdateEntityByIdState(
    val isLoading: Boolean = false, val entity: Entity? = null, val error: String? = ""
)