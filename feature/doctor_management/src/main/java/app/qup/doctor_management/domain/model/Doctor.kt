package app.qup.doctor_management.domain.model

import app.qup.doctor_management.data.remote.dto.general.ConductsOPDAtEntity
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet

data class Doctor(
    val appUserId: String,
    val communicationType: List<String>,
    val conductsOPDAtEntity: List<ConductsOPDAtEntity>,
    val createdAt: Long,
    val doctorId: String,
    val firstName: String,
    val lastModified: Long,
    val lastName: String,
    val mobileNumber: Long,
    val qualificationDegreeSet: List<QualificationDegreeSet>,
    val registrationMonth: Int,
    val registrationNumber: String,
    val registrationYear: Int,
    val specialitySet: List<SpecialitySet>
)