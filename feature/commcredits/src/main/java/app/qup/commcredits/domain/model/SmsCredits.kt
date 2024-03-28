package app.qup.commcredits.domain.model

data class SmsCredits(
    val currentAvailableCreditBalance: Int,
    val rechargeRequestList: List<SmsRechargeRequestModel>
)