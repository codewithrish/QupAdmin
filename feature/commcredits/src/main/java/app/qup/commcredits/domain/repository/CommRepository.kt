package app.qup.commcredits.domain.repository

import app.qup.commcredits.data.remote.dto.general.NotificationRechargeRequest
import app.qup.commcredits.data.remote.dto.general.SmsRechargeRequest
import app.qup.commcredits.data.remote.dto.request.ApproveNotificationCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.ApproveSmsCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.data.remote.dto.response.NotificationCreditsResponseDto
import app.qup.commcredits.data.remote.dto.response.SmsCreditsResponseDto
import app.qup.commcredits.data.remote.dto.response.TopUpSmsResponseDto
import retrofit2.Response

interface CommRepository {
    suspend fun getSmsCredits(): Response<SmsCreditsResponseDto>
    suspend fun getNotificationCredits(): Response<NotificationCreditsResponseDto>
    suspend fun topUpSms(topUpSmsRequestDto: TopUpSmsRequestDto): Response<TopUpSmsResponseDto>
    suspend fun approveSmsCredits(requestId: String, approveSmsCreditsRequestDto: ApproveSmsCreditsRequestDto): Response<SmsRechargeRequest>
    suspend fun approveNotificationCredits(requestId: String, approveNotificationCreditsRequestDto: ApproveNotificationCreditsRequestDto): Response<NotificationRechargeRequest>
}