package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.Link
import app.qup.doctor_management.domain.model.SpecialityCategory

data class SpecialityCategoryResponseDto(
    val active: Boolean?,
    val description: String?,
    val iconName: String?,
    val links: List<Link>?,
    val name: String?,
    val specialityCategoryId: String?
)

fun SpecialityCategoryResponseDto.toSpecialityCategory(): SpecialityCategory {
    return SpecialityCategory(
        active = active ?: false,
        description = description ?: "",
        iconName = iconName ?: "",
        name = name ?: "",
        specialityCategoryId = specialityCategoryId ?: ""
    )
}