package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.entity_management.data.remote.dto.general.LandlineNumber
import com.codewithrish.entity_management.databinding.ListItemEntityLandlineBinding
import javax.inject.Inject

class LandlineAdapter @Inject constructor() : ListAdapter<LandlineNumber, LandlineAdapter.LandlineViewHolder>(LandlineDiffUtil()) {

    var onStdCodeTyping: ((String, Int) -> Unit)? = null
    var onLandlineTyping: ((Long, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandlineViewHolder {
        val binding = ListItemEntityLandlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LandlineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LandlineViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class LandlineViewHolder(private val binding: ListItemEntityLandlineBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LandlineNumber) {
            binding.etStdCode.addTextChangedListener {
                val input = it.toString()
                onStdCodeTyping?.invoke(input, bindingAdapterPosition)
            }
            binding.etNumber.addTextChangedListener {
                val input = it.toString()
                onLandlineTyping?.invoke(if (input.isNotEmpty()) input.toLong() else 0, bindingAdapterPosition)
            }
        }
    }
    class LandlineDiffUtil: DiffUtil.ItemCallback<LandlineNumber>() {
        override fun areItemsTheSame(
            oldItem: LandlineNumber,
            newItem: LandlineNumber
        ): Boolean = oldItem == newItem
        override fun areContentsTheSame(
            oldItem: LandlineNumber,
            newItem: LandlineNumber
        ): Boolean = oldItem == newItem
    }
}