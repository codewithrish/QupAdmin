package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.data.remote.dto.general.SmsRechargeRequest
import app.qup.commcredits.data.remote.dto.general.toSmsRechargeRequestModel
import app.qup.commcredits.domain.model.SmsCredits


data class SmsCreditsResponseDto(
    val currentAvailableCreditBalance: Int?,
    val rechargeRequestList: List<SmsRechargeRequest>?
)

fun SmsCreditsResponseDto.toSmsCredits(): SmsCredits {
    return SmsCredits(
        currentAvailableCreditBalance = currentAvailableCreditBalance ?: 0,
        rechargeRequestList = rechargeRequestList?.map { it1 -> it1.toSmsRechargeRequestModel() } ?: mutableListOf()
    )
}