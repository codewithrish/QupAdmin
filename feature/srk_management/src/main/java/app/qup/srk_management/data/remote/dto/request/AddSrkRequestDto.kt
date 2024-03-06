package app.qup.srk_management.data.remote.dto.request

import app.qup.util.common.DEFAULT_LANGUAGE_ID

data class AddSrkRequestDto(
    val bloodGroup: String? = null,
    val dateOfBirth: String? = null,
    val emailId: String? = null,
    val firstName: String? = null,
    val gender: String? = null,
    val lastName: String? = null,
    val mobileNumber: Long? = null,
    val preferredLanguageId: String? = DEFAULT_LANGUAGE_ID,
    val prefix: String? = null,
    val profilePicturePath: String? = null,
    val requestedRole: String? = null,
    val volunteerForBloodDonation: String? = "LATER"
)