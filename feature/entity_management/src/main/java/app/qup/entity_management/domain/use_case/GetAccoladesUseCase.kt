package app.qup.entity_management.domain.use_case

import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.response.toEntityAccoladeSet
import app.qup.entity_management.domain.repository.EntityRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAccoladesUseCase @Inject constructor(
    private val entityRepository: EntityRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(AccoladesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                entityRepository.getAccolades().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                AccoladesState(
                                    isLoading = false,
                                    accolades = it.body()?.map { it1 -> it1.toEntityAccoladeSet() }
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
    val isLoading: Boolean = false, val accolades: List<EntityAccoladeSet>? = null, val error: String? = ""
)