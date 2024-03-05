package app.qup.doctor_management.data.remote.dto.general

data class UserInfo(
    val appUserId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val mobileNumber: Long? = null,
    val prefix: String? = null
)