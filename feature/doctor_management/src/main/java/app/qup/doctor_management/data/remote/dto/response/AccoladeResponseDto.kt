package app.qup.doctor_management.data.remote.dto.response

import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.data.remote.dto.general.Link

data class AccoladeResponseDto(
    val accoladeId: String?,
    val active: Boolean?,
    val description: String?,
    val iconName: String?,
    val links: List<Link>?,
    val type: String?
)

fun AccoladeResponseDto.toAccoladeSet(): AccoladesSet {
    return AccoladesSet(
        accoladeTypeId = accoladeId,
        accoladeType = type,
        iconName = iconName,
        notes = null
    )
}