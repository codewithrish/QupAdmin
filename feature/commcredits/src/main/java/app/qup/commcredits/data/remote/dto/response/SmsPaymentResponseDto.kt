package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.data.remote.dto.general.ChangedSmsRechargeRequest

data class SmsPaymentResponseDto(
    val changedRechargeRequest: ChangedSmsRechargeRequest?,
    val currentAvailableCreditBalance: Int?
)