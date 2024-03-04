package app.qup.reception_management.data.remote.dto.general

import app.qup.reception_management.domain.model.Reception

data class ReceptionResource(
    val associatedRoles: List<String>?,
    val bloodGroup: String?,
    val dateOfBirth: Long?,
    val emailId: String?,
    val firstName: String?,
    val gender: String?,
    val lastName: String?,
    val links: List<Link>?,
    val mobileNumber: Long?,
    val preferredLanguageId: String?,
    val preferredLanguageName: String?,
    val prefix: String?,
    val profilePicturePath: String?,
    val region: List<String>?,
    val userId: String?,
    val volunteerForBloodDonation: String?
)

fun ReceptionResource.toReception(): Reception {
    return Reception(
        associatedRoles = associatedRoles ?: mutableListOf(),
        bloodGroup = bloodGroup ?: "",
        dateOfBirth = dateOfBirth ?: 0,
        emailId = emailId ?: "",
        firstName = firstName ?: "",
        gender = gender ?: "",
        lastName = lastName ?: "",
        mobileNumber = mobileNumber ?: 0,
        preferredLanguageId = preferredLanguageId ?: "",
        preferredLanguageName = preferredLanguageName ?: "",
        prefix = prefix ?: "",
        profilePicturePath = profilePicturePath ?: "",
        region = region ?: mutableListOf(),
        userId = userId ?: "",
        volunteerForBloodDonation = volunteerForBloodDonation ?: ""
    )
}