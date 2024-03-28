package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.data.remote.dto.general.ChangedNotificationRechargeRequest
import app.qup.commcredits.data.remote.dto.general.Link

data class NotificationPaymentResponseDto(
    val changedRechargeRequest: ChangedNotificationRechargeRequest?,
    val currentAvailableCreditBalance: Int?,
    val links: List<Link>?
)