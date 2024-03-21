package app.qup.summary.data.remote.dto.response

import app.qup.summary.data.remote.dto.general.Link
import app.qup.summary.domain.model.Summary

data class SummaryResponseDto(
    val bookingDailySlotId: String?,
    val bookingQueueSlotName: String?,
    val bookingSlotDatePerUTC: Long?,
    val businessOPDStatusDesc: String?,
    val businessSideOPDSlotStatus: String?,
    val dailySlotSummaryId: String?,
    val doctorFullName: String?,
    val doctorId: String?,
    val entityId: String?,
    val entityName: String?,
    val links: List<Link>?,
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
    val totalNoOfBookingsMade: Int?
)

fun SummaryResponseDto.toSummary(): Summary {
    return Summary(
        bookingDailySlotId = bookingDailySlotId ?: "",
        bookingQueueSlotName = bookingQueueSlotName ?: "",
        bookingSlotDatePerUTC = bookingSlotDatePerUTC ?: 0,
        businessOPDStatusDesc = businessOPDStatusDesc ?: "",
        businessSideOPDSlotStatus = businessSideOPDSlotStatus ?: "",
        dailySlotSummaryId = dailySlotSummaryId ?: "",
        doctorFullName = doctorFullName ?: "",
        doctorId = doctorId ?: "",
        entityId = entityId ?: "",
        entityName = entityName ?: "",
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
        totalNoOfBookingsMade = totalNoOfBookingsMade ?: 0
    )
}