package app.qup.authentication.data.remote.dto.response

import app.qup.authentication.domain.model.ResendOtpInfo

data class ResendOtpResponseDto(
    val firstName: String?,
    val lastName: String?,
    val mobileNumber: Long?,
    val prefix: String?
)

fun ResendOtpResponseDto.toResendOtpInfo(): ResendOtpInfo {
    return ResendOtpInfo(
        firstName = firstName?:"",
        lastName = lastName?:"",
        mobileNumber = mobileNumber?:0,
        prefix = prefix?:""
    )
}