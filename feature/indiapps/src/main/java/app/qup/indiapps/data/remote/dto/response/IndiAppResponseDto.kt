package app.qup.indiapps.data.remote.dto.response

import app.qup.indiapps.data.remote.dto.general.Link
import app.qup.indiapps.domain.model.IndiApp

data class IndiAppResponseDto(
    val appDownloadLink: String?,
    val appId: String?,
    val appName: String?,
    val appSubscriptionType: String?,
    val indiClinicAppMappingId: String?,
    val links: List<Link>?,
    val mappedCity: String?,
    val mappedClinicIds: List<String>?,
    val noOfFreeBookingsAllowed: Int?,
    val selfBookingAllowedForNonPrime: Boolean?,
    val smsSenderId: String?,
)

fun IndiAppResponseDto.toIndiApp(): IndiApp {
    return IndiApp(
        appDownloadLink = appDownloadLink ?: "",
        appId = appId ?: "",
        appName = appName ?: "",
        appSubscriptionType = appSubscriptionType ?: "",
        indiClinicAppMappingId = indiClinicAppMappingId ?: "",
        mappedCity = mappedCity ?: "",
        mappedClinicIds = mappedClinicIds ?: mutableListOf(),
        noOfFreeBookingsAllowed = noOfFreeBookingsAllowed ?: -1,
        selfBookingAllowedForNonPrime = selfBookingAllowedForNonPrime ?: false,
        smsSenderId = smsSenderId ?: ""
    )
}