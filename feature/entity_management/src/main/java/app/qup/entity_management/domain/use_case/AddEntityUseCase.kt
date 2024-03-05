package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.request.AddEntityRequestDto
import app.qup.entity_management.data.remote.dto.response.toEntity
import app.qup.entity_management.domain.model.Entity
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEntityUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
        addEntityRequestDto: AddEntityRequestDto
    ) = channelFlow {
        send(AddEntityState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.addEntity(addEntityRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                AddEntityState(
                                    isLoading = false,
                                    entity = it.body()?.toEntity()
                                )
                            )
                        } else {
                            send(
                                AddEntityState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(AddEntityState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class AddEntityState(
    val isLoading: Boolean = false, val entity: Entity? = null, val error: String? = ""
)