package app.qup.doctor_management.domain.model

data class SpecialityCategory(
    val active: Boolean? = false,
    val description: String? = "",
    val iconName: String? = "",
    val name: String? = "",
    val specialityCategoryId: String? = ""
)