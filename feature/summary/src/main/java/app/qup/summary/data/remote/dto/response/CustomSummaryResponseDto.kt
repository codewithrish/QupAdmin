package app.qup.summary.data.remote.dto.response

import app.qup.summary.data.remote.dto.general.DailySummaryReportResource
import app.qup.summary.data.remote.dto.general.toDailySummaryReport
import app.qup.summary.domain.model.CustomSummary

data class CustomSummaryResponseDto(
    val bookingSlotDatePerUTC: Long?,
    val businessOPDStatusDesc: String?,
    val businessSideOPDSlotStatus: String?,
    val dailySummaryReportResourceList: List<DailySummaryReportResource>?,
    val doctorFullName: String?,
    val doctorId: String?,
    val entityId: String?,
    val entityName: String?,
    val mainSlotName: String?,
    val noOfCurrentDummyPatients: Int?,
    val noOfDummyPatientsToBeAddedAtSlotCreation: Int?,
    val noOfOnlineBookingsAllowed: Int?,
    val noOfOnlineBookingsMade: Int?,
    val opdEndTimeSecsFromMidnight: Int?,
    val opdStartTimeSecsFromMidnight: Int?,
    val parentBookingQueueId: String?,
    val parentBookingQueueSlotScheduleId: String?,
    val queueWithType: String?,
    val totalNoOfBookingsAllowed: Int?,
    val totalNoOfBookingsMade: Int?,
    val totalNoOfWalkingBookingMade: Int?,
)

fun CustomSummaryResponseDto.toCustomSummary(): CustomSummary {
    return CustomSummary(
        bookingSlotDatePerUTC = bookingSlotDatePerUTC ?: 0,
        businessOPDStatusDesc = businessOPDStatusDesc ?: "",
        businessSideOPDSlotStatus = businessSideOPDSlotStatus ?: "",
        dailySummaryReportResourceList = dailySummaryReportResourceList?.map { it1 -> it1.toDailySummaryReport() } ?: mutableListOf(),
        doctorFullName = doctorFullName ?: "",
        doctorId = doctorId ?: "",
        entityId = entityId ?: "",
        entityName = entityName ?: "",
        mainSlotName = mainSlotName ?: "",
        noOfCurrentDummyPatients = noOfCurrentDummyPatients ?: 0,
        noOfDummyPatientsToBeAddedAtSlotCreation = noOfDummyPatientsToBeAddedAtSlotCreation ?: 0,
        noOfOnlineBookingsAllowed = noOfOnlineBookingsAllowed ?: 0,
        noOfOnlineBookingsMade = noOfOnlineBookingsMade ?: 0,
        opdEndTimeSecsFromMidnight = opdEndTimeSecsFromMidnight ?: 0,
        opdStartTimeSecsFromMidnight = opdStartTimeSecsFromMidnight ?: 0,
        parentBookingQueueId = parentBookingQueueId ?: "",
        parentBookingQueueSlotScheduleId = parentBookingQueueSlotScheduleId ?: "",
        queueWithType = queueWithType ?: "",
        totalNoOfBookingsAllowed = totalNoOfBookingsAllowed ?: 0,
        totalNoOfBookingsMade = totalNoOfBookingsMade ?: 0,
        totalNoOfWalkingBookingMade = totalNoOfWalkingBookingMade ?: 0,
    )
}