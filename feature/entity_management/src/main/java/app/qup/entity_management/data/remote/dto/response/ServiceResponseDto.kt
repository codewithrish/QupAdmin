package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import app.qup.entity_management.data.remote.dto.general.Link

data class ServiceResponseDto(
    val active: Boolean?,
    val description: String?,
    val links: List<Link>?,
    val name: String?,
    val serviceId: String?
)

fun ServiceResponseDto.toEntityServiceSet(): EntityServiceSet {
    return EntityServiceSet(
        entityServiceId = serviceId,
        name = name,
        description = description,
    )
}