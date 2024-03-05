package app.qup.entity_management.data.remote.dto.general

import app.qup.entity_management.domain.model.EntityR

data class EntityResource(
    val active: Boolean?,
    val area: String?,
    val city: String?,
    val entityAdminInfo: EntityAdminInfo?,
    val entityId: String?,
    val entityType: EntityType?,
    val landlineNumber: List<LandlineNumber>?,
    val links: List<Link>?,
    val mobileNumber: List<Long>?,
    val name: String?
)

fun EntityResource.toEntity(): EntityR {
    return EntityR(
        active = active ?: false,
        area = area ?: "",
        city = city ?: "",
        entityAdminInfo = entityAdminInfo ?: EntityAdminInfo(),
        entityId = entityId ?: "",
        entityType = entityType ?: EntityType(),
        landlineNumber = landlineNumber ?: mutableListOf(),
        mobileNumber = mobileNumber ?: mutableListOf(),
        name = name ?: ""
    )
}