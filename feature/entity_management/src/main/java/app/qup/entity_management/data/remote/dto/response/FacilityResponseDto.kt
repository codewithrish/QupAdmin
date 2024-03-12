package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.FacilitySet
import app.qup.entity_management.data.remote.dto.general.Link

data class FacilityResponseDto(
    val active: Boolean?,
    val description: String?,
    val facilityId: String?,
    val links: List<Link>?,
    val name: String?
)

fun FacilityResponseDto.toFacilitySet(): FacilitySet {
    return FacilitySet(
        facilityId = facilityId,
        name = name,
        description = description,
    )
}