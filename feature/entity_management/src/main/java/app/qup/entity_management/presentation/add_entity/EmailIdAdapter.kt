package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.util.common.isValidEmail
import com.codewithrish.entity_management.databinding.ListItemEntityEmailIdBinding
import javax.inject.Inject

class EmailIdAdapter @Inject constructor() : ListAdapter<String, EmailIdAdapter.EmailIdViewHolder>(EmailIdDiffUtil()) {

    var onEmailTyping: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailIdViewHolder {
        val binding = ListItemEntityEmailIdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailIdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailIdViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class EmailIdViewHolder(private val binding: ListItemEntityEmailIdBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.etEmailId.setText(item)
            binding.etEmailId.addTextChangedListener {
                val input = it.toString()
                if (input.isNotEmpty()) {
                    binding.tilEmailId.error = if (!input.isValidEmail()) "Enter Valid Email" else null
                }
                onEmailTyping?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class EmailIdDiffUtil: DiffUtil.ItemCallback<String>() {
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