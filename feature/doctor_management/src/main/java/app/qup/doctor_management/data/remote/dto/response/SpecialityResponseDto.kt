package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.AssociatedCheckUpType
import app.qup.doctor_management.data.remote.dto.general.Link
import app.qup.doctor_management.domain.model.Speciality
import app.qup.doctor_management.domain.model.SpecialityCategory

data class SpecialityResponseDto(
    val active: Boolean?,
    val associatedCheckUpTypes: List<AssociatedCheckUpType>?,
    val description: String?,
    val iconName: String?,
    val links: List<Link>?,
    val name: String?,
    val popular: Boolean?,
    val specialityApplicableForKids: Boolean?,
    val specialityCategory: SpecialityCategoryResponseDto?,
    val specialityId: String?
)

fun SpecialityResponseDto.toSpeciality(): Speciality {
    return Speciality(
        active = active ?: false,
        associatedCheckUpTypes = associatedCheckUpTypes ?: mutableListOf(),
        description = description ?: "",
        iconName = iconName ?: "",
        name = name ?: "",
        popular = popular ?: false,
        specialityApplicableForKids = specialityApplicableForKids ?: false,
        specialityCategory = specialityCategory?.toSpecialityCategory() ?: SpecialityCategory(),
        specialityId = specialityId ?: ""
    )
}