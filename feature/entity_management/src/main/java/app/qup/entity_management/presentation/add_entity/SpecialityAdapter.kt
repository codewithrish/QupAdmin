package app.qup.entity_management.presentation.add_entity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.entity_management.data.remote.dto.general.EntitySpecialitySet
import com.codewithrish.entity_management.databinding.ListItemEntitySpecialityBinding
import javax.inject.Inject

class SpecialityAdapter @Inject constructor(
    private val specialities: List<EntitySpecialitySet>
) : ListAdapter<EntitySpecialitySet, SpecialityAdapter.SpecialityViewHolder>(SpecialityDiffUtil()) {

    var onSpecialitySelected: ((EntitySpecialitySet, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialityViewHolder {
        val binding = ListItemEntitySpecialityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialityViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SpecialityViewHolder(private val binding: ListItemEntitySpecialityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EntitySpecialitySet) {
            val specialitiesAdapter: ArrayAdapter<EntitySpecialitySet?> =
                object : ArrayAdapter<EntitySpecialitySet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, specialities.toList()) {
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
            binding.spSelectSpeciality.setAdapter(specialitiesAdapter)
            binding.spSelectSpeciality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onSpecialitySelected?.invoke((specialitiesAdapter.getItem(position) as EntitySpecialitySet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
    class SpecialityDiffUtil: DiffUtil.ItemCallback<EntitySpecialitySet>() {
        override fun areItemsTheSame(
            oldItem: EntitySpecialitySet,
            newItem: EntitySpecialitySet
        ): Boolean = oldItem.specialityId == newItem.specialityId
        override fun areContentsTheSame(
            oldItem: EntitySpecialitySet,
            newItem: EntitySpecialitySet
        ): Boolean = oldItem == newItem
    }
}