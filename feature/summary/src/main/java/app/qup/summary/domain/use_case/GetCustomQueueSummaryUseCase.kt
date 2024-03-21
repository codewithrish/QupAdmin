package app.qup.summary.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.data.remote.dto.response.toCustomSummary
import app.qup.summary.domain.model.CustomSummary
import app.qup.summary.domain.repository.SummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCustomQueueSummaryUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    operator fun invoke(
        summaryRequestDto: SummaryRequestDto
    ) = channelFlow {
        send(CustomQueueSummaryState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                summaryRepository.getCustomQueueSummary(summaryRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                CustomQueueSummaryState(
                                    isLoading = false,
                                    customSummaryList = it.body()?.map { it1 -> it1.toCustomSummary() }
                                )
                            )
                        } else {
                            send(
                                CustomQueueSummaryState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(CustomQueueSummaryState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class CustomQueueSummaryState(
    val isLoading: Boolean = false, val customSummaryList: List<CustomSummary>? = null, val error: String? = ""
)