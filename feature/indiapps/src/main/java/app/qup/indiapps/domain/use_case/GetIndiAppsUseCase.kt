package app.qup.indiapps.domain.use_case

import app.qup.indiapps.data.remote.dto.response.toIndiApp
import app.qup.indiapps.domain.model.IndiApp
import app.qup.indiapps.domain.repository.IndiRepository
import app.qup.network.common.parseErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetIndiAppsUseCase @Inject constructor(
    private val indiRepository: IndiRepository
) {
    operator fun invoke() = channelFlow {
        send(GetIndiAppsState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                indiRepository.getIndiApps().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                GetIndiAppsState(
                                    isLoading = false,
                                    indiApps = it.body()?.map { it1 -> it1.toIndiApp() }
                                )
                            )
                        } else {
                            send(
                                GetIndiAppsState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(GetIndiAppsState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class GetIndiAppsState(
    val isLoading: Boolean = false, val indiApps: List<IndiApp>? = null, val error: String? = ""
)