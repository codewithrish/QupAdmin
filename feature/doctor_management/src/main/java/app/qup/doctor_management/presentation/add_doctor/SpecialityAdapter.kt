package app.qup.doctor_management.presentation.add_doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.qup.doctor_management.data.remote.dto.general.SpecialitySet
import app.qup.doctor_management.databinding.ListItemSpecialityBinding
import app.qup.doctor_management.domain.model.Speciality
import app.qup.doctor_management.domain.model.SpecialityCategory
import javax.inject.Inject

class SpecialityAdapter @Inject constructor(
    private val allCategories: List<SpecialityCategory>,
    private val allSpecialities: List<SpecialitySet>,
    private val specialities: List<Speciality>
): ListAdapter<SpecialitySet, SpecialityAdapter.SpecialityViewHolder>(SpecialityDiffUtil()) {

    var selectedSpeciality: ((SpecialitySet, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialityViewHolder {
        val binding = ListItemSpecialityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialityViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    inner class SpecialityViewHolder(private val binding: ListItemSpecialityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SpecialitySet) {
            val specialityCategoryAdapter: ArrayAdapter<SpecialityCategory?> =
                object : ArrayAdapter<SpecialityCategory?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, allCategories) {
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
            binding.spSelectCategory.adapter = specialityCategoryAdapter

            val specialityAdapter: ArrayAdapter<SpecialitySet?> =
                object : ArrayAdapter<SpecialitySet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, allSpecialities) {
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
            binding.spSelectSpeciality.adapter = specialityAdapter

            val selectSpecialityIndex = allSpecialities.map { it1 -> it1.specialityId }.indexOf(item.specialityId)
            binding.spSelectSpeciality.setSelection(selectSpecialityIndex, true)

            val findCategoryId = specialities[selectSpecialityIndex].specialityCategory.specialityCategoryId
            if (!findCategoryId.isNullOrEmpty()) {
                binding.spSelectCategory.setSelection(allCategories.map { it1 -> it1.specialityCategoryId }.indexOf(findCategoryId) + 1, true)
            }

            binding.spSelectSpeciality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedSpeciality?.invoke((specialityAdapter.getItem(position) as SpecialitySet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
    class SpecialityDiffUtil: DiffUtil.ItemCallback<SpecialitySet>() {
        override fun areItemsTheSame(oldItem: SpecialitySet, newItem: SpecialitySet): Boolean = oldItem.specialityId == newItem.specialityId
        override fun areContentsTheSame(oldItem: SpecialitySet, newItem: SpecialitySet): Boolean = oldItem == newItem
    }
}