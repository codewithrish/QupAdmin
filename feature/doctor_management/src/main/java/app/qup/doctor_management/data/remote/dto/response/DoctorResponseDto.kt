package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.ConductsOPDAtEntity
import app.qup.doctor_management.data.remote.dto.general.Link
import app.qup.doctor_management.data.remote.dto.general.OtherInfo
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet
import app.qup.doctor_management.data.remote.dto.general.UserInfo
import app.qup.doctor_management.domain.model.Doctor

data class DoctorResponseDto(
    val accoladesSet: List<AccoladesSet>?,
    val active: Boolean?,
    val communicationType: List<String>?,
    val conductsOPDAtEntity: List<ConductsOPDAtEntity>?,
    val doctorId: String?,
    val doctorSpecificDegree: String?,
    val emailId: String?,
    val gmbLink: String?,
    val links: List<Link>?,
    val medicalAchievements: List<String>?,
    val nonMedicalAchievements: List<String>?,
    val otherInfoList: List<OtherInfo>?,
    val practicesInCity: List<String>?,
    val qualificationDegreeSet: List<QualificationDegreeSet>?,
    val registrationMonth: Int?,
    val registrationNumber: String?,
    val registrationYear: Int?,
    val s3DoctorPhotoPath: String?,
    val specialitySet: List<SpecialitySet>?,
    val userInfo: UserInfo?,
    val videoConsultationLink: String?
)

fun DoctorResponseDto.toDoctor(): Doctor {
    return Doctor(
        accoladesSet = accoladesSet ?: mutableListOf(),
        active = active ?: false,
        communicationType = communicationType ?: mutableListOf(),
        conductsOPDAtEntity = conductsOPDAtEntity ?: mutableListOf(),
        doctorId = doctorId ?: "",
        doctorSpecificDegree = doctorSpecificDegree ?: "",
        emailId = emailId ?: "",
        gmbLink = gmbLink ?: "",
        medicalAchievements = medicalAchievements ?: mutableListOf(),
        nonMedicalAchievements = nonMedicalAchievements ?: mutableListOf(),
        otherInfoList = otherInfoList ?: mutableListOf(),
        practicesInCity = practicesInCity ?: mutableListOf(),
        qualificationDegreeSet = qualificationDegreeSet ?: mutableListOf(),
        registrationMonth = registrationMonth ?: 0,
        registrationNumber = registrationNumber ?: "",
        registrationYear = registrationYear ?: 0,
        s3DoctorPhotoPath = s3DoctorPhotoPath ?: "",
        specialitySet = specialitySet ?: mutableListOf(),
        userInfo = userInfo ?: UserInfo(),
        videoConsultationLink = videoConsultationLink ?: ""
    )
}