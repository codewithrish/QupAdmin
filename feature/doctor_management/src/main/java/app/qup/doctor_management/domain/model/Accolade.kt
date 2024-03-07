package app.qup.doctor_management.domain.model

data class Accolade(
    val accoladeId: String,
    val active: Boolean,
    val description: String,
    val iconName: String,
    val type: String
)