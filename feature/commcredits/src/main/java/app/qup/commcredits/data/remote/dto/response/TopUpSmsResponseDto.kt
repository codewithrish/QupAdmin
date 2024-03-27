package app.qup.commcredits.data.remote.dto.response

import app.qup.commcredits.domain.model.TopUpSms

data class TopUpSmsResponseDto(
    val balanceLeftForAllocation: Int?
)

fun TopUpSmsResponseDto.toTopUpSms(): TopUpSms {
    return TopUpSms(
        balanceLeftForAllocation = balanceLeftForAllocation ?: 0
    )
}