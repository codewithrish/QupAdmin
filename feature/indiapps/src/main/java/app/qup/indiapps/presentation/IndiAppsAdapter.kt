package app.qup.indiapps.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.indiapps.databinding.ListItemIndiappBinding
import app.qup.indiapps.domain.model.IndiApp

class IndiAppsAdapter : ListAdapter<IndiApp, IndiAppsAdapter.IndiAppsViewHolder>(IndiAppsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndiAppsViewHolder {
        val binding = ListItemIndiappBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndiAppsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndiAppsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class IndiAppsViewHolder(private val binding: ListItemIndiappBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IndiApp) {
            binding.tvAppName.text = item.appName.ifEmpty { "NA" }
            binding.tvSubscriptionType.text = item.appSubscriptionType.ifEmpty { "NA" }
            binding.tvNoOfFreeBookingsAllowed.text = item.noOfFreeBookingsAllowed.toString().ifEmpty { "NA" }
            binding.tvSelfBookingNonPrime.text = item.selfBookingAllowedForNonPrime.toString().ifEmpty { "NA" }
            binding.tvSmsSenderId.text = item.smsSenderId.ifEmpty { "NA" }
            binding.tvAppDownloadLink.text = item.appDownloadLink.ifEmpty { "NA" }
        }
    }

    class IndiAppsDiffUtil : DiffUtil.ItemCallback<IndiApp>() {
        override fun areItemsTheSame(oldItem: IndiApp, newItem: IndiApp): Boolean = oldItem.appId == newItem.appId
        override fun areContentsTheSame(oldItem: IndiApp, newItem: IndiApp): Boolean = oldItem == newItem
    }
}