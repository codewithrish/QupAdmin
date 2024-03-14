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
import app.qup.entity_management.data.remote.dto.general.FacilitySet
import com.codewithrish.entity_management.databinding.ListItemEntityFacilityBinding
import javax.inject.Inject

class FacilityAdapter @Inject constructor(
    private val facilities: List<FacilitySet>
) : ListAdapter<FacilitySet, FacilityAdapter.FacilityViewHolder>(FacilityDiffUtil()) {

    var onFacilitySelected: ((FacilitySet, Int) -> Unit)? = null
    var onFacilityDescType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilityViewHolder {
        val binding = ListItemEntityFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacilityViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class FacilityViewHolder(private val binding: ListItemEntityFacilityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FacilitySet) {
            val facilitiesAdapter: ArrayAdapter<FacilitySet?> =
                object : ArrayAdapter<FacilitySet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, facilities.toList()) {
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
            binding.spSelectFacility.setAdapter(facilitiesAdapter)
            binding.spSelectFacility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onFacilitySelected?.invoke((facilitiesAdapter.getItem(position) as FacilitySet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            binding.spSelectFacility.setSelection(facilities.map { it1 -> it1.facilityId }.indexOf(item.facilityId))

            binding.etDescription.setText(item.description)
            binding.etDescription.addTextChangedListener {
                onFacilityDescType?.invoke(it.toString(), bindingAdapterPosition)
            }
        }
    }
    class FacilityDiffUtil: DiffUtil.ItemCallback<FacilitySet>() {
        override fun areItemsTheSame(
            oldItem: FacilitySet,
            newItem: FacilitySet
        ): Boolean = oldItem.facilityId == newItem.facilityId
        override fun areContentsTheSame(
            oldItem: FacilitySet,
            newItem: FacilitySet
        ): Boolean = oldItem == newItem
    }
}