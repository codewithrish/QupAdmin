package app.qup.doctor_management.presentation.add_doctor

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
import app.qup.doctor_management.data.remote.dto.general.QualificationDegreeSet
import app.qup.doctor_management.databinding.ListItemDegreeBinding
import javax.inject.Inject

class DegreesAdapter @Inject constructor(
    private val  allDegrees: List<QualificationDegreeSet>
) : ListAdapter<QualificationDegreeSet, DegreesAdapter.DegreesViewHolder>(DegreesDiffUtil()) {

    var onDegreeSelected: ((QualificationDegreeSet, Int) -> Unit)? = null
    var onLocationType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DegreesViewHolder {
        val binding = ListItemDegreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DegreesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DegreesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DegreesViewHolder(private val binding: ListItemDegreeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QualificationDegreeSet) {
            val degreesAdapter: ArrayAdapter<QualificationDegreeSet?> =
                object : ArrayAdapter<QualificationDegreeSet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, allDegrees) {
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
            binding.spSelectDegree.setAdapter(degreesAdapter)
            binding.spSelectDegree.setSelection(allDegrees.map { it1 -> it1.educationDegreeId }.indexOf(item.educationDegreeId))

            binding.spSelectDegree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onDegreeSelected?.invoke((degreesAdapter.getItem(position) as QualificationDegreeSet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            binding.etAddLocation.setText(item.location)
            binding.etAddLocation.addTextChangedListener {
                val input = it.toString()
                onLocationType?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class DegreesDiffUtil: DiffUtil.ItemCallback<QualificationDegreeSet>() {
        override fun areItemsTheSame(
            oldItem: QualificationDegreeSet,
            newItem: QualificationDegreeSet
        ): Boolean = oldItem.educationDegreeId == newItem.educationDegreeId
        override fun areContentsTheSame(
            oldItem: QualificationDegreeSet,
            newItem: QualificationDegreeSet
        ): Boolean = oldItem == newItem
    }
}