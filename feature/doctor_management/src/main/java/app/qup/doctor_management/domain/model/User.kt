package app.qup.doctor_management.domain.model

data class User (
    val associatedRoles: List<String>,
    val bloodGroup: String,
    val dateOfBirth: Long,
    val emailId: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val mobileNumber: Long,
    val preferredLanguageId: String,
    val preferredLanguageName: String,
    val prefix: String,
    val profilePicturePath: String,
    val region: List<String>,
    val userId: String,
    val volunteerForBloodDonation: String
)