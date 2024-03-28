package app.qup.commcredits.data.repository

import app.qup.commcredits.data.remote.api.CommApi
import app.qup.commcredits.data.remote.dto.general.NotificationRechargeRequest
import app.qup.commcredits.data.remote.dto.general.SmsRechargeRequest
import app.qup.commcredits.data.remote.dto.request.ApproveNotificationCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.ApproveSmsCreditsRequestDto
import app.qup.commcredits.data.remote.dto.request.MarkPaymentDoneRequestDto
import app.qup.commcredits.data.remote.dto.request.TopUpSmsRequestDto
import app.qup.commcredits.data.remote.dto.response.NotificationCreditsResponseDto
import app.qup.commcredits.data.remote.dto.response.NotificationPaymentResponseDto
import app.qup.commcredits.data.remote.dto.response.SmsCreditsResponseDto
import app.qup.commcredits.data.remote.dto.response.SmsPaymentResponseDto
import app.qup.commcredits.data.remote.dto.response.TopUpSmsResponseDto
import app.qup.commcredits.domain.repository.CommRepository
import retrofit2.Response
import javax.inject.Inject

class CommRepositoryImpl @Inject constructor(
    private val commApi: CommApi
) : CommRepository {
    override suspend fun getSmsCredits(): Response<SmsCreditsResponseDto> {
        return commApi.getSmsCredits()
    }

    override suspend fun getNotificationCredits(): Response<NotificationCreditsResponseDto> {
        return commApi.getNotificationCredits()
    }

    override suspend fun topUpSms(topUpSmsRequestDto: TopUpSmsRequestDto): Response<TopUpSmsResponseDto> {
        return commApi.topUpSms(topUpSmsRequestDto)
    }

    override suspend fun approveSmsCredits(
        requestId: String,
        approveSmsCreditsRequestDto: ApproveSmsCreditsRequestDto
    ): Response<SmsRechargeRequest> {
        return commApi.approveSmsCredits(requestId, approveSmsCreditsRequestDto)
    }

    override suspend fun markSmsPaymentPaid(
        requestId: String,
        markPaymentDoneRequestDto: MarkPaymentDoneRequestDto
    ): Response<SmsPaymentResponseDto> {
        return commApi.markSmsPaymentPaid(requestId, markPaymentDoneRequestDto)
    }

    override suspend fun approveNotificationCredits(
        requestId: String,
        approveNotificationCreditsRequestDto: ApproveNotificationCreditsRequestDto
    ): Response<NotificationRechargeRequest> {
        return commApi.approveNotificationCredits(requestId, approveNotificationCreditsRequestDto)
    }

    override suspend fun markNotificationPaymentPaid(
        requestId: String,
        markPaymentDoneRequestDto: MarkPaymentDoneRequestDto
    ): Response<NotificationPaymentResponseDto> {
        return commApi.markNotificationPaymentPaid(requestId, markPaymentDoneRequestDto)
    }
}