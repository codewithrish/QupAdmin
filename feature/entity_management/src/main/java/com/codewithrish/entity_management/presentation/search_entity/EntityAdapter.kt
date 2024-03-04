package com.codewithrish.entity_management.presentation.search_entity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codewithrish.entity_management.databinding.ListItemEntityBinding
import com.codewithrish.entity_management.domain.model.Entity

class EntityAdapter : PagingDataAdapter<Entity, EntityAdapter.DoctorViewHolder>(DoctorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ListItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DoctorViewHolder(private val binding: ListItemEntityBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Entity?) {
            item?.let { entity ->
                binding.tvEntityNameType.text = "${entity.name} (${entity.entityType.type})"
                binding.tvEntityAddress.text = "${entity.area}, ${entity.city}"
                // Entity Contact Info
                binding.tvEntityContact.isVisible = entity.landlineNumber.isNotEmpty() || entity.mobileNumber.isNotEmpty()
                val entityContactBuilder = StringBuilder()
                if (entity.landlineNumber.isNotEmpty()) {
                    entityContactBuilder.append("Landline : ")
                    entityContactBuilder.append(entity.landlineNumber.joinToString(", ") { it1 -> "${it1.stdCode}-${it1.landlineNumber}" })
                    entityContactBuilder.append(", ")
                }
                if (entity.mobileNumber.isNotEmpty()) {
                    entityContactBuilder.append("Mobile : ")
                    entityContactBuilder.append(entity.mobileNumber.joinToString(", "))
                }
                binding.tvEntityContact.text = entityContactBuilder.toString()
                // Entity Admin Info
                binding.tvEntityAdmin.isVisible = entity.entityAdminInfo.entityAdminId != null
                val adminInfoBuilder = StringBuilder()
                adminInfoBuilder.append("Admin: ")
                adminInfoBuilder.append(entity.entityAdminInfo.name)
                adminInfoBuilder.append(" ")
                adminInfoBuilder.append(entity.entityAdminInfo.mobileNumber)
                binding.tvEntityAdmin.text = adminInfoBuilder.toString()
            }
        }
    }

    class DoctorDiffUtil : DiffUtil.ItemCallback<Entity>() {
        override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem.entityId == newItem.entityId
        override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem == newItem
    }
}