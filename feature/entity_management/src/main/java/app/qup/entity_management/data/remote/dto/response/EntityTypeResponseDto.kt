package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.Link

data class EntityTypeResponseDto(
    val active: Boolean?,
    val description: String?,
    val entityTypeId: String?,
    val links: List<Link>?,
    val onlineBookingAllowed: Boolean?,
    val type: String?
)

fun EntityTypeResponseDto.toEntityType(): EntityType {
    return EntityType(
        entityTypeId = entityTypeId,
        type = type
    )
}