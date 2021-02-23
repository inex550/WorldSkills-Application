package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.OperationsListItemBinding
import com.example.worldskills.models.Operation
import java.text.SimpleDateFormat
import java.util.*

class OperationsAdapter: RecyclerView.Adapter<OperationsAdapter.ViewHolder>() {

    inner class ViewHolder(
            val binding: OperationsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(operation: Operation) {
            binding.nameTv.text = operation.name
            binding.dateTv.text = sdf.format(operation.date)
            binding.cashTv.text = operation.cash.toString()
        }
    }

    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

    var data: List<Operation> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OperationsListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val operation = data[position]
        holder.bind(operation)
    }

    override fun getItemCount(): Int = data.size
}