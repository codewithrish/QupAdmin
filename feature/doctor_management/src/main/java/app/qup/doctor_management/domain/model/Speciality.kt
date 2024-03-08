package app.qup.doctor_management.domain.model

import app.qup.doctor_management.data.remote.dto.general.AssociatedCheckUpType
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet

data class Speciality(
    val active: Boolean = false,
    val associatedCheckUpTypes: List<AssociatedCheckUpType> = emptyList(),
    val description: String = "",
    val iconName: String = "",
    val name: String = "",
    val popular: Boolean = false,
    val specialityApplicableForKids: Boolean = false,
    val specialityCategory: SpecialityCategory = SpecialityCategory(),
    val specialityId: String = ""
)

fun Speciality.toSpecialitySet() : SpecialitySet {
    return SpecialitySet(
        name = name,
        primary = false,
        specialityId = specialityId
    )
}