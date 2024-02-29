package app.qup.authentication.data.remote.dto.response

import app.qup.authentication.data.remote.dto.general.Link
import app.qup.authentication.domain.model.SignInInfo

data class SignInResponseDto(
    val currentRole: String?,
    val firstName: String?,
    val lastName: String?,
    val links: List<Link>?,
    val mobileNumber: Long?,
    val preferredLanguageId: String?,
    val prefix: String?,
    val userId: String?
)
fun SignInResponseDto.toSignInInfo(): SignInInfo {
    return SignInInfo(
        currentRole = currentRole?:"",
        firstName = firstName?:"",
        lastName = lastName?:"",
        mobileNumber = mobileNumber?:0,
        preferredLanguageId = preferredLanguageId?:"",
        prefix = prefix?:"",
        userId = userId?:""
    )
}