package app.qup.doctor_management.domain.model

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.ConductsOPDAtEntity
import app.qup.doctor_management.data.remote.dto.general.OtherInfo
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet
import app.qup.doctor_management.data.remote.dto.general.UserInfo

data class Doctor(
    val accoladesSet: List<AccoladesSet>,
    val active: Boolean,
    val communicationType: List<String>,
    val conductsOPDAtEntity: List<ConductsOPDAtEntity>,
    val doctorId: String,
    val doctorSpecificDegree: String,
    val emailId: String,
    val gmbLink: String,
    val medicalAchievements: List<String>,
    val nonMedicalAchievements: List<String>,
    val otherInfoList: List<OtherInfo>,
    val practicesInCity: List<String>,
    val qualificationDegreeSet: List<QualificationDegreeSet>,
    val registrationMonth: Int,
    val registrationNumber: String,
    val registrationYear: Int,
    val s3DoctorPhotoPath: String,
    val specialitySet: List<SpecialitySet>,
    val userInfo: UserInfo,
    val videoConsultationLink: String
)