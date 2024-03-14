package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithrish.entity_management.databinding.ListItemEntityMobileBinding
import javax.inject.Inject

class MobileNoAdapter @Inject constructor() : ListAdapter<Long, MobileNoAdapter.MobileNoViewHolder>(MobileNoDiffUtil()) {

    var onMobileTyping: ((Long, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MobileNoViewHolder {
        val binding = ListItemEntityMobileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MobileNoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MobileNoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MobileNoViewHolder(private val binding: ListItemEntityMobileBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Long) {
            binding.etMobileNumber.setText(item.toString())
            binding.etMobileNumber.addTextChangedListener {
                val input = it.toString()
                if (input.isNotEmpty()) {
                    binding.tilMobileNumber.error = if (input.length != 10) "Enter Valid Number" else null
                    onMobileTyping?.invoke(input.toLong(), bindingAdapterPosition)
                }
            }
        }
    }
    class MobileNoDiffUtil: DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(
            oldItem: Long,
            newItem: Long
        ): Boolean = oldItem == newItem
        override fun areContentsTheSame(
            oldItem: Long,
            newItem: Long
        ): Boolean = oldItem == newItem
    }
}