package app.qup.commcredits.data.remote.dto.request

data class ApproveNotificationCreditsRequestDto(
    val adminNote: String? = null,
    val approvedNoOfNotificationCredits: Int? = null,
)