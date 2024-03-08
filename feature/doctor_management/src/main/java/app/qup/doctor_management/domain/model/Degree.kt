package app.qup.doctor_management.domain.model

import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet

data class Degree(
    val active: Boolean = false,
    val description: String = "",
    val educationDegreeId: String = "",
    val name: String = ""
)

fun Degree.toQualificationDegreeSet(): QualificationDegreeSet {
    return QualificationDegreeSet(
        educationDegreeId = educationDegreeId,
        location = description,
        name = name,
        primary = false
    )
}