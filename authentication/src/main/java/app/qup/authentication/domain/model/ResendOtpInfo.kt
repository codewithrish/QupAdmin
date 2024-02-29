package app.qup.authentication.domain.model

data class ResendOtpInfo(
    val firstName: String,
    val lastName: String,
    val mobileNumber: Long,
    val prefix: String
)