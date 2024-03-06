package app.qup.reception_management.presentation.search_reception

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.reception_management.domain.model.Reception
import app.qup.reception_management.databinding.ListItemReceptionBinding
import app.qup.util.common.millisToDateString

class ReceptionAdapter : ListAdapter<Reception, ReceptionAdapter.DoctorViewHolder>(DoctorDiffUtil()) {

    var onClick: ((Reception) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ListItemReceptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DoctorViewHolder(private val binding: ListItemReceptionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Reception?) {
            item?.let { reception ->
                binding.tvReceptionName.text = "${reception.firstName} ${reception.lastName}"
                binding.tvMobileNumber.text = reception.mobileNumber.toString()
                binding.tvGender.text = reception.gender
                binding.tvDob.text = millisToDateString(reception.dateOfBirth)

                itemView.setOnClickListener {
                    onClick?.invoke(item)
                }
            }
        }
    }

    class DoctorDiffUtil : DiffUtil.ItemCallback<Reception>() {
        override fun areItemsTheSame(oldItem: Reception, newItem: Reception): Boolean = oldItem.userId == newItem.userId
        override fun areContentsTheSame(oldItem: Reception, newItem: Reception): Boolean = oldItem == newItem
    }
}