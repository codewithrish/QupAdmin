package app.qup.summary.domain.model

data class CustomSummary(
    val bookingSlotDatePerUTC: Long,
    val businessOPDStatusDesc: String,
    val businessSideOPDSlotStatus: String,
    val dailySummaryReportResourceList: List<DailySummaryReport>,
    val doctorFullName: String,
    val doctorId: String,
    val entityId: String,
    val entityName: String,
    val mainSlotName: String,
    val noOfCurrentDummyPatients: Int,
    val noOfDummyPatientsToBeAddedAtSlotCreation: Int,
    val noOfOnlineBookingsAllowed: Int,
    val noOfOnlineBookingsMade: Int,
    val opdEndTimeSecsFromMidnight: Int,
    val opdStartTimeSecsFromMidnight: Int,
    val parentBookingQueueId: String,
    val parentBookingQueueSlotScheduleId: String,
    val queueWithType: String,
    val totalNoOfBookingsAllowed: Int,
    val totalNoOfBookingsMade: Int,
    val totalNoOfWalkingBookingMade: Int
)