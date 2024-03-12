package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.response.toEntityServiceSet
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(ServicesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getServices().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                ServicesState(
                                    isLoading = false,
                                    entityServices = it.body()?.map { it1 -> it1.toEntityServiceSet() }
                                )
                            )
                        } else {
                            send(
                                ServicesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(ServicesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class ServicesState(
    val isLoading: Boolean = false, val entityServices: List<EntityServiceSet>? = null, val error: String? = ""
)