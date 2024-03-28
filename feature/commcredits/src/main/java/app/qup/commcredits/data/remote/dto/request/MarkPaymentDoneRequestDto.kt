package app.qup.commcredits.data.remote.dto.request

data class MarkPaymentDoneRequestDto(
    val adminNote: String?,
    val amountPaid: Int?
)