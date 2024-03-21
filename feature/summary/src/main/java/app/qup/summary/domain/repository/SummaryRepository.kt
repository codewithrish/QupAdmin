package app.qup.summary.domain.repository

import app.qup.summary.data.remote.dto.general.OpdStatusValue
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.data.remote.dto.response.CustomSummaryResponseDto
import app.qup.summary.data.remote.dto.response.SummaryResponseDto
import retrofit2.Response

interface SummaryRepository {
    suspend fun getQueueSummary(summaryRequestDto: SummaryRequestDto): Response<List<SummaryResponseDto>>
    suspend fun getOpdStatusValues(): Response<Map<String, OpdStatusValue>>
    suspend fun getCustomQueueSummary(summaryRequestDto: SummaryRequestDto): Response<List<CustomSummaryResponseDto>>
    suspend fun getCustomOpdStatusValues(): Response<Map<String, OpdStatusValue>>
}