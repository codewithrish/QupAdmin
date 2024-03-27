package app.qup.commcredits.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationRechargeRequestModel(
    val adminNotes: List<AdminNoteModel>,
    val amountReceivable: Double,
    val doctorMobileNumber: Long,
    val doctorName: String,
    val entityLocation: String,
    val entityName: String,
    val noOfNotificationCredits: Int,
    val notificationSubscriptionId: String,
    val rechargeRequestId: String,
    val rechargeStatus: String,
    val rechargeStatusDisplayName: String,
    val requestNote: String,
    val requestedDate: String
): Parcelable