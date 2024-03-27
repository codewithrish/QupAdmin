package app.qup.commcredits.data.remote.dto.request

data class ApproveSmsCreditsRequestDto(
    val adminNote: String? = null,
    val approvedNoOfSMSCredits: Int? = null,
)