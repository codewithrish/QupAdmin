package app.qup.indiapps.domain.model

data class IndiApp(
    val appDownloadLink: String,
    val appId: String,
    val appName: String,
    val appSubscriptionType: String,
    val indiClinicAppMappingId: String,
    val mappedCity: String,
    val mappedClinicIds: List<String>,
    val noOfFreeBookingsAllowed: Int,
    val selfBookingAllowedForNonPrime: Boolean,
    val smsSenderId: String
)