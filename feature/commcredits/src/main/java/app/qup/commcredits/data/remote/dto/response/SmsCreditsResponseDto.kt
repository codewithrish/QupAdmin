package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.data.remote.dto.general.SmsRechargeRequest


data class SmsCreditsResponseDto(
    val currentAvailableCreditBalance: Int?,
    val rechargeRequestList: List<SmsRechargeRequest>?
)