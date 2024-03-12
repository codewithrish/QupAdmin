package app.qup.entity_management.data.remote.dto.general

data class AssociatedCheckUpType(
    val active: Boolean?,
    val checkUpTypeId: String?,
    val description: String?,
    val links: List<Link>?,
    val name: String?
)