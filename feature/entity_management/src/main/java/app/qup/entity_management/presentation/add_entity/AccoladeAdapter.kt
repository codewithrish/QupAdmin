package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.entity_management.data.remote.dto.general.EntityAccoladeSet
import com.codewithrish.entity_management.databinding.ListItemEntityAccoladeBinding
import javax.inject.Inject

class AccoladeAdapter @Inject constructor(
    private val accolades: List<EntityAccoladeSet>
) : ListAdapter<EntityAccoladeSet, AccoladeAdapter.AccoladeViewHolder>(AccoladeDiffUtil()) {

    var onAccoladeSelected: ((EntityAccoladeSet, Int) -> Unit)? = null
    var onAccoladeDescType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccoladeViewHolder {
        val binding = ListItemEntityAccoladeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccoladeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccoladeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class AccoladeViewHolder(private val binding: ListItemEntityAccoladeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntityAccoladeSet) {
            val accoladesAdapter: ArrayAdapter<EntityAccoladeSet?> =
                object : ArrayAdapter<EntityAccoladeSet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, accolades.toList()) {
                    override fun getView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.text = getItem(position)?.name
                        return view
                    }

                    override fun getDropDownView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.setPadding(16,16,16,16)
                        view.text = getItem(position)?.name
                        return view
                    }
                }
            binding.spSelectAccolades.setAdapter(accoladesAdapter)
            binding.spSelectAccolades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onAccoladeSelected?.invoke((accoladesAdapter.getItem(position) as EntityAccoladeSet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            binding.etDescription.addTextChangedListener {
                onAccoladeDescType?.invoke(it.toString(), bindingAdapterPosition)
            }
        }
    }
    class AccoladeDiffUtil: DiffUtil.ItemCallback<EntityAccoladeSet>() {
        override fun areItemsTheSame(
            oldItem: EntityAccoladeSet,
            newItem: EntityAccoladeSet
        ): Boolean = oldItem.entityAccoladeId == newItem.entityAccoladeId
        override fun areContentsTheSame(
            oldItem: EntityAccoladeSet,
            newItem: EntityAccoladeSet
        ): Boolean = oldItem == newItem
    }
}