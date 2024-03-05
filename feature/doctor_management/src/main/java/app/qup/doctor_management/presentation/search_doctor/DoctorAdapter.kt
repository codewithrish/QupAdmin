package app.qup.doctor_management.presentation.search_doctor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.qup.doctor_management.databinding.ListItemDoctorBinding
import app.qup.doctor_management.domain.model.DoctorR
import app.qup.util.common.millisToDateString

class DoctorAdapter : PagingDataAdapter<DoctorR, DoctorAdapter.DoctorViewHolder>(DoctorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ListItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DoctorViewHolder(private val binding: ListItemDoctorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: DoctorR?) {
            item?.let { doctor ->
                binding.tvDoctorName.text = "${doctor.firstName} ${doctor.lastName}"
                binding.tvMobileNumber.text = doctor.mobileNumber.toString()
                binding.tvPrimarySpecDeg.text = "${doctor.specialitySet.filter { it1 -> it1.primary == true }[0].name}, ${doctor.qualificationDegreeSet.filter { it1 -> it1.primary == true }[0].name}"
                binding.tvRegNo.text = doctor.registrationNumber
                binding.tvProfileDates.text = "Creation : ${millisToDateString(doctor.createdAt)}, Modification : ${millisToDateString(doctor.lastModified)}"
            }
        }
    }

    class DoctorDiffUtil : DiffUtil.ItemCallback<DoctorR>() {
        override fun areItemsTheSame(oldItem: DoctorR, newItem: DoctorR): Boolean = oldItem.doctorId == newItem.doctorId
        override fun areContentsTheSame(oldItem: DoctorR, newItem: DoctorR): Boolean = oldItem == newItem
    }
}