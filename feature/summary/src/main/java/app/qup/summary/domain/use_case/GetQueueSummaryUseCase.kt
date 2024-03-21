package app.qup.summary.domain.use_case

import app.qup.network.common.parseErrorResponse
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.data.remote.dto.response.toSummary
import app.qup.summary.domain.model.Summary
import app.qup.summary.domain.repository.SummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetQueueSummaryUseCase @Inject constructor(
    private val summaryRepository: SummaryRepository
) {
    operator fun invoke(
        summaryRequestDto: SummaryRequestDto
    ) = channelFlow {
        send(QueueSummaryState(isLoading = true))
        try {
            withContext(Dispatchers.IO) {
                summaryRepository.getQueueSummary(summaryRequestDto).also {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            send(
                                QueueSummaryState(
                                    isLoading = false,
                                    summaryList = it.body()?.map { it1 -> it1.toSummary() }
                                )
                            )
                        } else {
                            send(
                                QueueSummaryState(
                                    isLoading = false, error = parseErrorResponse(it.errorBody())
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            send(QueueSummaryState(isLoading = false, error = e.localizedMessage))
        }
    }
}

data class QueueSummaryState(
    val isLoading: Boolean = false, val summaryList: List<Summary>? = null, val error: String? = ""
)