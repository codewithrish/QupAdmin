package app.qup.reception_management.data.remote.dto.general

data class Page(
    val number: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)