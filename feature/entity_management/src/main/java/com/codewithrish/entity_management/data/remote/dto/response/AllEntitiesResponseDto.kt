package com.codewithrish.entity_management.data.remote.dto.response

import com.codewithrish.entity_management.data.remote.dto.general.EntityResource
import com.codewithrish.entity_management.data.remote.dto.general.Page

data class AllEntitiesResponseDto (
    val _embedded: AllEntitiesEmbedded,
    val page: Page
) {
    data class AllEntitiesEmbedded(
        val entitySearchResourceList: List<EntityResource>
    )
}