package app.qup.commcredits.data.remote.dto.general

import app.qup.commcredits.domain.model.NotificationRechargeRequestModel

data class NotificationRechargeRequest(
    val adminNotes: List<AdminNote>?,
    val amountReceivable: Double?,
    val doctorMobileNumber: Long?,
    val doctorName: String?,
    val entityLocation: String?,
    val entityName: String?,
    val links: List<Link>?,
    val noOfNotificationCredits: Int?,
    val notificationSubscriptionId: String?,
    val rechargeRequestId: String?,
    val rechargeStatus: String?,
    val rechargeStatusDisplayName: String?,
    val requestNote: String?,
    val requestedDate: String?,
)

fun NotificationRechargeRequest.toNotificationRechargeRequestModel(): NotificationRechargeRequestModel {
    return NotificationRechargeRequestModel(
        adminNotes = adminNotes?.map { it1 -> it1.toAdminNoteModel() } ?: mutableListOf(),
        amountReceivable = amountReceivable ?: 0.toDouble(),
        doctorMobileNumber = doctorMobileNumber ?: 0,
        doctorName = doctorName ?: "",
        entityLocation = entityLocation ?: "",
        entityName = entityName ?: "",
        noOfNotificationCredits = noOfNotificationCredits ?: 0,
        notificationSubscriptionId = notificationSubscriptionId ?: "",
        rechargeRequestId = rechargeRequestId ?: "",
        rechargeStatus = rechargeStatus ?: "",
        rechargeStatusDisplayName = rechargeStatusDisplayName ?: "",
        requestNote = requestNote ?: "",
        requestedDate = requestedDate ?: "",
    )
}