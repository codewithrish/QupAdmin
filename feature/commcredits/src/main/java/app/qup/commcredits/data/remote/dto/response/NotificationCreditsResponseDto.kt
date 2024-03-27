package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.data.remote.dto.general.NotificationRechargeRequest

data class NotificationCreditsResponseDto(
    val currentAvailableNotificationCreditBalance: Int?,
    val notificationRechargeRequestList: List<NotificationRechargeRequest>?
)