package app.qup.srk_management.presentation.search_srk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.qup.srk_management.databinding.ListItemSrkBinding
import app.qup.srk_management.domain.model.Srk
import app.qup.util.common.millisToDateString

class SrkPaginatedAdapter : PagingDataAdapter<Srk, SrkPaginatedAdapter.DoctorViewHolder>(DoctorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ListItemSrkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DoctorViewHolder(private val binding: ListItemSrkBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Srk?) {
            item?.let { srk ->
                binding.tvSrkName.text = "${srk.firstName} ${srk.lastName}"
                binding.tvMobileNumber.text = srk.mobileNumber.toString()
                binding.tvGender.text = srk.gender
                binding.tvDob.text = millisToDateString(srk.dateOfBirth)
            }
        }
    }

    class DoctorDiffUtil : DiffUtil.ItemCallback<Srk>() {
        override fun areItemsTheSame(oldItem: Srk, newItem: Srk): Boolean = oldItem.userId == newItem.userId
        override fun areContentsTheSame(oldItem: Srk, newItem: Srk): Boolean = oldItem == newItem
    }
}