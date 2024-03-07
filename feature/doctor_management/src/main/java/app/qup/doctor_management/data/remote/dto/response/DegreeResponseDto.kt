package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.Link
import app.qup.doctor_management.domain.model.Degree

data class DegreeResponseDto(
    val active: Boolean?,
    val description: String?,
    val educationDegreeId: String?,
    val links: List<Link>?,
    val name: String?
)

fun DegreeResponseDto.toDegree() : Degree {
    return Degree(
        active = active ?: false,
        description = description ?: "",
        educationDegreeId = educationDegreeId ?: "",
        name = name ?: ""
    )
}