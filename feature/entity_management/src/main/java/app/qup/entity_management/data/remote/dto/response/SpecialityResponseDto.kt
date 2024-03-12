package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.AssociatedCheckUpType
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import app.qup.entity_management.data.remote.dto.general.Link

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

fun SpecialityResponseDto.toEntitySpecialitySet(): EntitySpecialitySet {
    return EntitySpecialitySet(
        specialityId = specialityId,
        name = name,
    )
}