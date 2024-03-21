package app.qup.summary.data.repository

import app.qup.summary.data.remote.api.SummaryApi
import app.qup.summary.data.remote.dto.general.OpdStatusValue
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.data.remote.dto.response.CustomSummaryResponseDto
import app.qup.summary.data.remote.dto.response.SummaryResponseDto
import app.qup.summary.domain.repository.SummaryRepository
import retrofit2.Response
import javax.inject.Inject

class SummaryRepositoryImpl @Inject constructor(
    private val summaryApi: SummaryApi
) : SummaryRepository {
    override suspend fun getQueueSummary(summaryRequestDto: SummaryRequestDto): Response<List<SummaryResponseDto>> {
        return summaryApi.getQueueSummary(summaryRequestDto)
    }

    override suspend fun getOpdStatusValues(): Response<Map<String, OpdStatusValue>> {
        return summaryApi.getOpdStatusValues()
    }

    override suspend fun getCustomQueueSummary(summaryRequestDto: SummaryRequestDto): Response<List<CustomSummaryResponseDto>> {
        return summaryApi.getCustomQueueSummary(summaryRequestDto)
    }

    override suspend fun getCustomOpdStatusValues(): Response<Map<String, OpdStatusValue>> {
        return summaryApi.getCustomOpdStatusValues()
    }
}