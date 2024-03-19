package app.qup.doctor_management.presentation.add_doctor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.doctor_management.databinding.ListItemMedicalAchievementBinding
import javax.inject.Inject

class MedAchievementAdapter @Inject constructor() : ListAdapter<String, MedAchievementAdapter.MedAchievementViewHolder>(MedAchievementDiffUtil()) {

    var onMedAchievementType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedAchievementViewHolder {
        val binding = ListItemMedicalAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedAchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedAchievementViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MedAchievementViewHolder(private val binding: ListItemMedicalAchievementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.etMedicalAchievements.setText(item)
            binding.etMedicalAchievements.addTextChangedListener {
                val input = it.toString()
                Log.d("TAG", "bind: $input")
                onMedAchievementType?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class MedAchievementDiffUtil: DiffUtil.ItemCallback<String>() {
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