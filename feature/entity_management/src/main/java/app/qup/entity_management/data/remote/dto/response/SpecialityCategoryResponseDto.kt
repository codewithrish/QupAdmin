package app.qup.entity_management.data.remote.dto.response

import app.qup.entity_management.data.remote.dto.general.Link

data class SpecialityCategoryResponseDto(
    val active: Boolean?,
    val description: String?,
    val iconName: String?,
    val links: List<Link>?,
    val name: String?,
    val specialityCategoryId: String?
)