package app.qup.commcredits.data.remote.dto.general

data class ChangedSmsRechargeRequest(
    val adminNotes: List<AdminNote>?,
    val amountReceivable: Double?,
    val commSubscriptionId: String?,
    val doctorMobileNumber: Long?,
    val doctorName: String?,
    val entityLocation: String?,
    val entityName: String?,
    val links: List<Link>?,
    val noOfSMSCredits: Int?,
    val rechargeRequestId: String?,
    val rechargeStatus: String?,
    val rechargeStatusDisplayName: String?,
    val requestNote: String?,
    val requestedDate: String?
)