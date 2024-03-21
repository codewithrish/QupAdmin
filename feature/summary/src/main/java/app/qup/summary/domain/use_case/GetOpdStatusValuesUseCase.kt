package app.qup.summary.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.summary.data.remote.dto.general.OpdStatusValue
import app.qup.summary.domain.repository.SummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetOpdStatusValuesUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    operator fun invoke(
    ) = channelFlow {
        send(OpdStatusValuesState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                summaryRepository.getOpdStatusValues().also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                OpdStatusValuesState(
                                    isLoading = false,
                                    values = it.body()
                                )
                            )
                        } else {
                            send(
                                OpdStatusValuesState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(OpdStatusValuesState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class OpdStatusValuesState(
    val isLoading: Boolean = false, val values: Map<String, OpdStatusValue>? = null, val error: String? = ""
)