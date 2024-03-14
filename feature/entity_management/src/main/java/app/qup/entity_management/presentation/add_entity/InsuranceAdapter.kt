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
import app.qup.entity_management.data.remote.dto.general.InsuranceCompanySet
import com.codewithrish.entity_management.databinding.ListItemEntityInsuranceBinding
import javax.inject.Inject

class InsuranceAdapter @Inject constructor(
    private val insuranceCompanies: List<InsuranceCompanySet>
) : ListAdapter<InsuranceCompanySet, InsuranceAdapter.InsuranceViewHolder>(InsuranceDiffUtil()) {

    var onInsuranceSelected: ((InsuranceCompanySet, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsuranceViewHolder {
        val binding = ListItemEntityInsuranceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InsuranceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InsuranceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class InsuranceViewHolder(private val binding: ListItemEntityInsuranceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InsuranceCompanySet) {
            val insuranceAdapter: ArrayAdapter<InsuranceCompanySet?> =
                object : ArrayAdapter<InsuranceCompanySet?>(itemView.context, app.qup.ui.R.layout.list_item_array_adapter, insuranceCompanies.toList()) {
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
            binding.spSelectInsurance.setAdapter(insuranceAdapter)
            binding.spSelectInsurance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onInsuranceSelected?.invoke((insuranceAdapter.getItem(position) as InsuranceCompanySet), bindingAdapterPosition)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            binding.spSelectInsurance.setSelection(insuranceCompanies.indexOf(item))
        }
    }
    class InsuranceDiffUtil: DiffUtil.ItemCallback<InsuranceCompanySet>() {
        override fun areItemsTheSame(
            oldItem: InsuranceCompanySet,
            newItem: InsuranceCompanySet
        ): Boolean = oldItem.insuranceCompanyId == newItem.insuranceCompanyId
        override fun areContentsTheSame(
            oldItem: InsuranceCompanySet,
            newItem: InsuranceCompanySet
        ): Boolean = oldItem == newItem
    }
}