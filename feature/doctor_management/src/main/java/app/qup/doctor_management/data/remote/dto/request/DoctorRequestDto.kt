package app.qup.doctor_management.data.remote.dto.request

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet

data class DoctorRequestDto(
    val accoladesSet: List<AccoladesSet>? = null,
    val bloodGroup: String? = null,
    val communicationType: List<String>? = null,
    val dateOfBirth: String? = null,
    val emailId: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: String? = null,
    val medicalAchievements: List<String>? = null,
    val mobileNumber: Long? = null,
    val nonMedicalAchievements: List<String>? = null,
    val preferredLanguageId: String? = null,
    val qualificationDegreeSet: List<QualificationDegreeSet>? = null,
    val registrationMonth: Int? = null,
    val registrationNumber: String? = null,
    val registrationYear: Int? = null,
    val specialitySet: List<SpecialitySet>? = null
)