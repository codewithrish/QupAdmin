package app.qup.commcredits.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.commcredits.databinding.ListItemSmsRechargeRequestBinding
import app.qup.commcredits.domain.model.SmsRechargeRequestModel

class SmsRechargeRequestAdapter: ListAdapter<SmsRechargeRequestModel, SmsRechargeRequestAdapter.SmsRechargeRequestViewHolder>(
    SmsRechargeRequestDiffUtil()
) {
    var onPaymentClick: ((SmsRechargeRequestModel) -> Unit)? = null
    var onAdminNotesClick: ((SmsRechargeRequestModel) -> Unit)? = null
    var onApproveCreditsClick: ((SmsRechargeRequestModel) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmsRechargeRequestViewHolder {
        val binding = ListItemSmsRechargeRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsRechargeRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmsRechargeRequestViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SmsRechargeRequestViewHolder(private val binding: ListItemSmsRechargeRequestBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SmsRechargeRequestModel) {
            binding.tvDoctorName.text = item.doctorName.ifEmpty { "NA" }
            binding.tvPhoneNumber.text = item.doctorMobileNumber.toString().ifEmpty { "NA" }
            binding.tvEntityName.text = item.entityName
            binding.tvEntityLocation.text = item.entityLocation
            binding.tvApproximateSmsCreditsRequested.text = "${item.noOfSMSCredits}".plus(if (item.requestNote.trim().isEmpty()) "" else " (${item.requestNote})")
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
            binding.btnApproveCredits.isEnabled = item.rechargeStatus != "APPROVED"
            binding.btnApproveCredits.text =  if (item.rechargeStatus == "APPROVED") "Approved" else "Approve Credits"
        }
    }

    class SmsRechargeRequestDiffUtil: DiffUtil.ItemCallback<SmsRechargeRequestModel>() {
        override fun areItemsTheSame(
            oldItem: SmsRechargeRequestModel,
            newItem: SmsRechargeRequestModel
        ): Boolean = oldItem.rechargeRequestId == newItem.rechargeRequestId
        override fun areContentsTheSame(
            oldItem: SmsRechargeRequestModel,
            newItem: SmsRechargeRequestModel
        ): Boolean = oldItem == newItem
    }
}