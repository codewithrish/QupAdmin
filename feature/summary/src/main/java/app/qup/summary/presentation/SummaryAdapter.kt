package app.qup.summary.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.summary.R
import app.qup.summary.common.*
import app.qup.summary.databinding.ListItemSummaryBinding
import app.qup.summary.domain.model.Summary
import app.qup.util.common.LOCAL_TIME_FORMAT
import app.qup.util.common.getMidNightTime
import app.qup.util.common.millisToDateString
import javax.inject.Inject

class SummaryAdapter @Inject constructor(
    private val dateTimeInMillis: Long = getMidNightTime()
) : ListAdapter<Summary, SummaryAdapter.SummaryViewHolder>(SummaryDiffUtil()) {

    var onItemClick: ((Summary) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ListItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SummaryViewHolder(private val binding: ListItemSummaryBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Summary) {
            binding.tvSno.text = "${adapterPosition + 1}."
            binding.tvDoctorName.text = item.doctorFullName.ifEmpty { "-" }
            binding.tvSlotNameTime.text = "${item.bookingQueueSlotName} " +
                    "(${millisToDateString(dateTimeInMillis + item.opdStartTimeSecsFromMidnight, LOCAL_TIME_FORMAT)}-" +
                    "${millisToDateString(dateTimeInMillis + item.opdEndTimeSecsFromMidnight, LOCAL_TIME_FORMAT)})".ifEmpty { "-" }
            binding.tvOpdStatus.text = item.businessOPDStatusDesc
            binding.tvBookingCount.text = "Bookings Made: [Online: ${item.noOfOnlineBookingsMade}, Walk-In: ${item.totalNoOfBookingsMade - item.noOfOnlineBookingsMade}, Total: ${item.totalNoOfBookingsMade}]".ifEmpty { "-" }
            binding.tvDummy.text = "Dummy Current/Total: ${item.noOfCurrentDummyPatients}/${item.noOfDummyPatientsToBeAddedAtSlotCreation}".ifEmpty { "-" }
            binding.tvQueueType.text = item.queueWithType.ifEmpty { "-" }
            binding.civOpdStatusColor.setImageResource(setOpdStatus(item.businessSideOPDSlotStatus))
            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }
    class SummaryDiffUtil: DiffUtil.ItemCallback<Summary>() {
        override fun areItemsTheSame(
            oldItem: Summary,
            newItem: Summary
        ): Boolean = oldItem.dailySlotSummaryId == newItem.dailySlotSummaryId
        override fun areContentsTheSame(
            oldItem: Summary,
            newItem: Summary
        ): Boolean = oldItem == newItem
    }

    private fun setOpdStatus(status: String) : Int {
        return when (status) {
            DID_NOT_VISIT, AUTO_PAUSE_WHEN_OPD_NOT_STARTED,
            YET_TO_OPEN, NOT_STARTED -> {
                R.color.status_yet_to_open
            }

            AVAILABLE, ACTIVE, PATIENT_CHECK_IN,
            CLINIC_CHECK_IN, SKIPPED, AUTO_PAUSED,
            AUTO_PAUSE_OPD, RE_START_OPD, START_OPD,
            STARTED, UNDO_CHECK_IN, GO,
            RESTART_ONLINE_BOOKING_FOR_SLOT -> {
                R.color.status_active
            }

            ON_LEAVE -> {
                R.color.status_on_leave
            }

            PAUSED, PAUSE_OPD, FILLING_FAST -> {
                R.color.status_fast
            }

            CLOSED, CANCELLED, STOPPED,
            STOP_ONLINE_BOOKING_FOR_SLOT -> {
                R.color.status_full
            }

            COMPLETED -> {
                R.color.status_done_green
            }

            CANCELLED_BY_DOCTOR, CANCELLED_BY_PATIENT -> {
                R.color.status_cancelled
            }

            else -> R.color.status_yet_to_open
        }
    }
}