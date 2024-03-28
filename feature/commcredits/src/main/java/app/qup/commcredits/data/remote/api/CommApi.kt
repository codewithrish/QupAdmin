package app.qup.commcredits.data.remote.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CommApi {
    @GET("notification-service/comm/sms/subscription/active/recharge-requests")
    suspend fun getSmsCredits(): Response<SmsCreditsResponseDto>
    @GET("notification-service/communication/notification/subscription/active/recharge-requests")
    suspend fun getNotificationCredits(): Response<NotificationCreditsResponseDto>
    @POST("notification-service/comm/sms/subscription/top-up/sms-credits")
    suspend fun topUpSms(@Body topUpSmsRequestDto: TopUpSmsRequestDto): Response<TopUpSmsResponseDto>
    @PUT("notification-service/comm/sms/subscription/recharge/{requestId}/approve")
    suspend fun approveSmsCredits(@Path("requestId") requestId: String, @Body approveSmsCreditsRequestDto: ApproveSmsCreditsRequestDto): Response<SmsRechargeRequest>
    @PUT("notification-service/comm/sms/subscription/recharge/{requestId}/payment-made")
    suspend fun markSmsPaymentPaid(@Path("requestId") requestId: String, @Body markPaymentDoneRequestDto: MarkPaymentDoneRequestDto): Response<SmsPaymentResponseDto>
    @PUT("notification-service/communication/notification/subscription/recharge/{requestId}/approve")
    suspend fun approveNotificationCredits(@Path("requestId") requestId: String, @Body approveNotificationCreditsRequestDto: ApproveNotificationCreditsRequestDto): Response<NotificationRechargeRequest>
    @PUT("notification-service/communication/notification/subscription/recharge/{requestId}/payment-made")
    suspend fun markNotificationPaymentPaid(@Path("requestId") requestId: String, @Body markPaymentDoneRequestDto: MarkPaymentDoneRequestDto): Response<NotificationPaymentResponseDto>
}