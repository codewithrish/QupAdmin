package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import app.qup.entity_management.data.remote.dto.general.Link

data class EntityAccoladeResponseDto(
    val active: Boolean?,
    val description: String?,
    val entityAccoladeId: String?,
    val iconName: String?,
    val links: List<Link>?,
    val type: String?
)

fun EntityAccoladeResponseDto.toEntityAccoladeSet(): EntityAccoladeSet {
    return EntityAccoladeSet(
        entityAccoladeId = entityAccoladeId,
        name = type,
        note = description
    )
}