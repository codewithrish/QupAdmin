package app.qup.authentication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInInfo(
    val currentRole: String,
    val firstName: String,
    val lastName: String,
    val mobileNumber: Long,
    val preferredLanguageId: String,
    val prefix: String,
    val userId: String
): Parcelable