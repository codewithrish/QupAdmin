package com.codewithrish.entity_management.data.remote.dto.general

import com.codewithrish.entity_management.domain.model.Entity

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

fun EntityResource.toEntity(): Entity {
    return Entity(
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