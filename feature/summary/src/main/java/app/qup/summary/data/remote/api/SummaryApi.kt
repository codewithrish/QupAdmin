package app.qup.summary.data.remote.api

import app.qup.summary.data.remote.dto.general.OpdStatusValue
import app.qup.summary.data.remote.dto.request.SummaryRequestDto
import app.qup.summary.data.remote.dto.response.CustomSummaryResponseDto
import app.qup.summary.data.remote.dto.response.SummaryResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SummaryApi {
    @POST("booking-service/reports/daily/summary")
    suspend fun getQueueSummary(@Body summaryRequestDto: SummaryRequestDto): Response<List<SummaryResponseDto>>
    @GET("provider-service/booking/queue/slot/business-opd-status/values")
    suspend fun getOpdStatusValues(): Response<Map<String, OpdStatusValue>>
    @POST("custom-booking-service/reports/daily/summary")
    suspend fun getCustomQueueSummary(@Body summaryRequestDto: SummaryRequestDto): Response<List<CustomSummaryResponseDto>>
    @GET("custom-booking-admin/custom/queue/slot/business-opd-status/values")
    suspend fun getCustomOpdStatusValues(): Response<Map<String, OpdStatusValue>>
}