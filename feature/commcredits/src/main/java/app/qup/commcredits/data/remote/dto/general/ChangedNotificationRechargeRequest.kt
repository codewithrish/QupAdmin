package app.qup.commcredits.data.remote.dto.general

data class ChangedNotificationRechargeRequest(
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