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
import app.qup.doctor_management.data.remote.dto.general.AccoladesSet
import app.qup.doctor_management.databinding.ListItemAccoladeBinding
import javax.inject.Inject

class AccoladesAdapter @Inject constructor(
    private val allAccolades: List<AccoladesSet>
): ListAdapter<AccoladesSet, AccoladesAdapter.AccoladesViewHolder>(AccoladesDiffUtil()) {

    var selectedAccolade: ((AccoladesSet, Int) -> Unit)? = null
    var onNoteType: ((String, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccoladesViewHolder {
        val binding = ListItemAccoladeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccoladesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccoladesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    inner class AccoladesViewHolder(private val binding: ListItemAccoladeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccoladesSet) {
            val accoladesAdapter: ArrayAdapter<AccoladesSet?> =
                object : ArrayAdapter<AccoladesSet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, allAccolades) {
                    override fun getView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.text = getItem(position)?.accoladeType
                        return view
                    }

                    override fun getDropDownView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup
                    ): View {
                        val view = super.getView(position, convertView, parent) as TextView
                        view.setPadding(16,16,16,16)
                        view.text = getItem(position)?.accoladeType
                        return view
                    }
                }
            binding.spSelectAccolades.adapter = accoladesAdapter

            binding.spSelectAccolades.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedAccolade?.invoke((accoladesAdapter.getItem(position) as AccoladesSet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            binding.etDescription.addTextChangedListener {
                val input = it.toString()
                onNoteType?.invoke(input, bindingAdapterPosition)
            }
        }
    }
    class AccoladesDiffUtil: DiffUtil.ItemCallback<AccoladesSet>() {
        override fun areItemsTheSame(oldItem: AccoladesSet, newItem: AccoladesSet): Boolean = oldItem.accoladeTypeId == newItem.accoladeTypeId
        override fun areContentsTheSame(oldItem: AccoladesSet, newItem: AccoladesSet): Boolean = oldItem == newItem
    }
}