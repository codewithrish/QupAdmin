package app.qup.entity_management.domain.model

import app.qup.entity_management.data.remote.dto.general.EntityAdminInfo
import app.qup.entity_management.data.remote.dto.general.EntityType
import app.qup.entity_management.data.remote.dto.general.LandlineNumber

data class EntityR(
    val active: Boolean,
    val area: String,
    val city: String,
    val entityAdminInfo: EntityAdminInfo,
    val entityId: String,
    val entityType: EntityType,
    val landlineNumber: List<LandlineNumber>,
    val mobileNumber: List<Long>,
    val name: String
)