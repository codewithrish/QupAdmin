package app.qup.commcredits.data.remote.dto.general

import app.qup.commcredits.domain.model.SmsRechargeRequestModel

data class SmsRechargeRequest(
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
    val requestedDate: String?,
)

fun SmsRechargeRequest.toSmsRechargeRequestModel() : SmsRechargeRequestModel{
    return SmsRechargeRequestModel(
        adminNotes = adminNotes?.map { it1 -> it1.toAdminNoteModel() } ?: mutableListOf(),
        amountReceivable = amountReceivable ?: 0.toDouble(),
        commSubscriptionId = commSubscriptionId ?: "",
        doctorMobileNumber = doctorMobileNumber ?: 0,
        doctorName = doctorName ?: "",
        entityLocation = entityLocation ?: "",
        entityName = entityName ?: "",
        noOfSMSCredits = noOfSMSCredits ?: 0,
        rechargeRequestId = rechargeRequestId ?: "",
        rechargeStatus = rechargeStatus ?: "",
        rechargeStatusDisplayName = rechargeStatusDisplayName ?: "",
        requestNote = requestNote ?: "",
        requestedDate = requestedDate ?: "",
    )
}