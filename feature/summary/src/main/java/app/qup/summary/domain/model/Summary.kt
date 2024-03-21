package app.qup.summary.domain.model

data class Summary(
    val bookingDailySlotId: String,
    val bookingQueueSlotName: String,
    val bookingSlotDatePerUTC: Long,
    val businessOPDStatusDesc: String,
    val businessSideOPDSlotStatus: String,
    val dailySlotSummaryId: String,
    val doctorFullName: String,
    val doctorId: String,
    val entityId: String,
    val entityName: String,
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
    val totalNoOfBookingsMade: Int
)