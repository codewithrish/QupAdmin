package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.Link
import app.qup.doctor_management.domain.model.Accolade

data class AccoladeResponseDto(
    val accoladeId: String?,
    val active: Boolean?,
    val description: String?,
    val iconName: String?,
    val links: List<Link>?,
    val type: String?
)

fun AccoladeResponseDto.toAccolade(): Accolade {
    return Accolade(
        accoladeId = accoladeId ?: "",
        active = active ?: false,
        description = description ?: "",
        iconName = iconName ?: "",
        type = type ?: ""
    )
}