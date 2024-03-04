package com.codewithrish.entity_management.domain.model

import com.codewithrish.entity_management.data.remote.dto.general.EntityAdminInfo
import com.codewithrish.entity_management.data.remote.dto.general.EntityType
import com.codewithrish.entity_management.data.remote.dto.general.LandlineNumber

data class Entity(
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