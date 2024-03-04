package app.qup.reception_management.data.remote.dto.response

import app.qup.reception_management.data.remote.dto.general.Page
import app.qup.reception_management.data.remote.dto.general.ReceptionResource

data class AllReceptionsResponseDto(
    val _embedded: AllReceptionsEmbedded,
    val page: Page
) {
    data class AllReceptionsEmbedded(
        val appUserDetailsResourceList : List<ReceptionResource>
    )
}
