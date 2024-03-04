package app.qup.srk_management.data.remote.dto.request

data class SearchUserRequestDto(
    val appRole: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)