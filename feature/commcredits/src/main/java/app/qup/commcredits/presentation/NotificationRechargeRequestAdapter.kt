package app.qup.commcredits.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.commcredits.databinding.ListItemNotificationRechargeRequestBinding
import app.qup.commcredits.domain.model.NotificationRechargeRequestModel

class NotificationRechargeRequestAdapter: ListAdapter<NotificationRechargeRequestModel, NotificationRechargeRequestAdapter.NotificationRechargeRequestViewHolder>(NotificationRechargeRequestDiffUtil()) {

    var onPaymentClick: ((NotificationRechargeRequestModel) -> Unit)? = null
    var onAdminNotesClick: ((NotificationRechargeRequestModel) -> Unit)? = null
    var onApproveCreditsClick: ((NotificationRechargeRequestModel) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRechargeRequestViewHolder {
        val binding = ListItemNotificationRechargeRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationRechargeRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationRechargeRequestViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NotificationRechargeRequestViewHolder(private val binding: ListItemNotificationRechargeRequestBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: NotificationRechargeRequestModel) {
            binding.tvDoctorName.text = item.doctorName
            binding.tvPhoneNumber.text = item.doctorMobileNumber.toString().ifEmpty { "NA" }
            binding.tvEntityName.text = item.entityName
            binding.tvEntityLocation.text = item.entityLocation
            binding.tvApproximateSmsCreditsRequested.text = "${item.noOfNotificationCredits}".plus(if (item.requestNote.trim().isEmpty()) "" else " (${item.requestNote})")
            binding.btnPayment.setOnClickListener {
                onPaymentClick?.invoke(item)
            }
            binding.btnAdminNotes.setOnClickListener {
                onAdminNotesClick?.invoke(item)
            }
            binding.btnApproveCredits.setOnClickListener {
                onApproveCreditsClick?.invoke(item)
            }

            binding.btnAdminNotes.isVisible = item.adminNotes.isNotEmpty()
            binding.btnPayment.isEnabled = item.rechargeStatus == "APPROVED"
            binding.btnApproveCredits.isEnabled = item.rechargeStatus != "APPROVED"
            binding.btnApproveCredits.text =  if (item.rechargeStatus == "APPROVED") "Approved" else "Approve Credits"
        }
    }

    class NotificationRechargeRequestDiffUtil: DiffUtil.ItemCallback<NotificationRechargeRequestModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationRechargeRequestModel,
            newItem: NotificationRechargeRequestModel
        ): Boolean = oldItem.rechargeRequestId == newItem.rechargeRequestId
        override fun areContentsTheSame(
            oldItem: NotificationRechargeRequestModel,
            newItem: NotificationRechargeRequestModel
        ): Boolean = oldItem == newItem
    }
}