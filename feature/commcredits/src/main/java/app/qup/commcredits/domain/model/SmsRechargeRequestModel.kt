package app.qup.commcredits.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsRechargeRequestModel(
    val adminNotes: List<AdminNoteModel>,
    val amountReceivable: Double,
    val commSubscriptionId: String,
    val doctorMobileNumber: Long,
    val doctorName: String,
    val entityLocation: String,
    val entityName: String,
    val noOfSMSCredits: Int,
    val rechargeRequestId: String,
    val rechargeStatus: String,
    val rechargeStatusDisplayName: String,
    val requestNote: String,
    val requestedDate: String,
): Parcelable