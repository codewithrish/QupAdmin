package app.qup.entity_management.presentation.add_entity

import android.util.Log
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
import app.qup.entity_management.data.remote.dto.general.EntityServiceSet
import com.codewithrish.entity_management.databinding.ListItemEntityServiceBinding
import javax.inject.Inject

class ServiceAdapter @Inject constructor(
    private val services: List<EntityServiceSet>
) : ListAdapter<EntityServiceSet, ServiceAdapter.ServiceViewHolder>(ServiceDiffUtil()) {

    var onServiceSelected: ((EntityServiceSet, Int) -> Unit)? = null
    var onServiceDescType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ListItemEntityServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ServiceViewHolder(private val binding: ListItemEntityServiceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntityServiceSet) {
            val facilitiesAdapter: ArrayAdapter<EntityServiceSet?> =
                object : ArrayAdapter<EntityServiceSet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, services.toList()) {
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
            binding.spSelectService.setAdapter(facilitiesAdapter)
            binding.spSelectService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onServiceSelected?.invoke((facilitiesAdapter.getItem(position) as EntityServiceSet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            binding.spSelectService.setSelection(services.map { it1 -> it1.entityServiceId }.indexOf(item.entityServiceId))

            binding.etDescription.setText(item.description)
            binding.etDescription.addTextChangedListener {
                onServiceDescType?.invoke(it.toString(), bindingAdapterPosition)
            }
        }
    }
    class ServiceDiffUtil: DiffUtil.ItemCallback<EntityServiceSet>() {
        override fun areItemsTheSame(
            oldItem: EntityServiceSet,
            newItem: EntityServiceSet
        ): Boolean = oldItem.entityServiceId == newItem.entityServiceId
        override fun areContentsTheSame(
            oldItem: EntityServiceSet,
            newItem: EntityServiceSet
        ): Boolean = oldItem == newItem
    }
}