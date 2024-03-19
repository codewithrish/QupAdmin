package app.qup.doctor_management.presentation.add_doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.doctor_management.databinding.ListItemNonMedicalAchievementBinding
import javax.inject.Inject

class NonMedAchievementAdapter @Inject constructor() : ListAdapter<String, NonMedAchievementAdapter.NonMedAchievementViewHolder>(NonMedAchievementDiffUtil()) {

    var onNonMedAchievementType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonMedAchievementViewHolder {
        val binding = ListItemNonMedicalAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NonMedAchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NonMedAchievementViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class NonMedAchievementViewHolder(private val binding: ListItemNonMedicalAchievementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.etNonMedicalAchievements.setText(item)
            binding.etNonMedicalAchievements.addTextChangedListener {
                val input = it.toString()
                onNonMedAchievementType?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class NonMedAchievementDiffUtil: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem
        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem
    }
}