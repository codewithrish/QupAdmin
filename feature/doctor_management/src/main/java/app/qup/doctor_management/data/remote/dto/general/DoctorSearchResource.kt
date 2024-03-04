package app.qup.doctor_management.data.remote.dto.general

import app.qup.doctor_management.domain.model.Doctor

data class DoctorSearchResource(
    val appUserId: String?,
    val communicationType: List<String>?,
    val conductsOPDAtEntity: List<ConductsOPDAtEntity>?,
    val createdAt: Long?,
    val doctorId: String?,
    val firstName: String?,
    val lastModified: Long?,
    val lastName: String?,
    val links: List<Link>?,
    val mobileNumber: Long?,
    val qualificationDegreeSet: List<QualificationDegreeSet>?,
    val registrationMonth: Int?,
    val registrationNumber: String?,
    val registrationYear: Int?,
    val specialitySet: List<SpecialitySet>?
)

fun DoctorSearchResource.toDoctor(): Doctor {
    return Doctor(
        appUserId = appUserId ?: "",
        communicationType = communicationType ?: mutableListOf(),
        conductsOPDAtEntity = conductsOPDAtEntity ?: mutableListOf(),
        createdAt = createdAt ?: 0,
        doctorId = doctorId ?: "",
        firstName = firstName ?: "",
        lastModified = lastModified ?: 0,
        lastName = lastName ?: "",
        mobileNumber = mobileNumber ?: 0,
        qualificationDegreeSet = qualificationDegreeSet ?: mutableListOf(),
        registrationMonth = registrationMonth ?: 0,
        registrationNumber = registrationNumber ?: "",
        registrationYear = registrationYear ?: 0,
        specialitySet = specialitySet ?: mutableListOf()
    )
}