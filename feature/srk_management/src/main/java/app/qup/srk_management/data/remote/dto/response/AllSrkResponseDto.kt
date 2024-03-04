package app.qup.srk_management.data.remote.dto.response

import app.qup.srk_management.data.remote.dto.general.Page
import app.qup.srk_management.data.remote.dto.general.SrkResource

data class AllSrkResponseDto(
    val _embedded: AllReceptionsEmbedded,
    val page: Page
) {
    data class AllReceptionsEmbedded(
        val appUserDetailsResourceList : List<SrkResource>
    )
}
