package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.EntityResource
import app.qup.entity_management.data.remote.dto.general.Page

data class AllEntitiesResponseDto (
    val _embedded: AllEntitiesEmbedded,
    val page: Page
) {
    data class AllEntitiesEmbedded(
        val entitySearchResourceList: List<EntityResource>
    )
}