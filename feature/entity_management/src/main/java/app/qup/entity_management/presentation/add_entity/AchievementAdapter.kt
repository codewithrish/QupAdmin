package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithrish.entity_management.databinding.ListItemEntityAchievementBinding
import javax.inject.Inject

class AchievementAdapter @Inject constructor() : ListAdapter<String, AchievementAdapter.AchievementViewHolder>(AchievementDiffUtil()) {

    var onAchievementTyping: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ListItemEntityAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class AchievementViewHolder(private val binding: ListItemEntityAchievementBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.etAchievement.addTextChangedListener {
                val input = it.toString()
                onAchievementTyping?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class AchievementDiffUtil: DiffUtil.ItemCallback<String>() {
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