package app.qup.doctor_management.data.remote.dto.request

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet

data class DoctorRequestDto(
    val accoladesSet: List<AccoladesSet>,
    val bloodGroup: String,
    val communicationType: List<String>,
    val dateOfBirth: String,
    val emailId: String,
    val firstName: String,
    val gender: String,
    val lastName: String,
    val medicalAchievements: List<String>,
    val mobileNumber: Long,
    val nonMedicalAchievements: List<String>,
    val preferredLanguageId: String,
    val qualificationDegreeSet: List<QualificationDegreeSet>,
    val registrationMonth: Int,
    val registrationNumber: String,
    val registrationYear: Int,
    val specialitySet: List<SpecialitySet>
)