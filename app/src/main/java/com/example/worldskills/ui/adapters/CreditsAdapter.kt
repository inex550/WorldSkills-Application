package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.CreditsListItemBinding
import com.example.worldskills.models.Credit
import java.text.SimpleDateFormat
import java.util.*

class CreditsAdapter: RecyclerView.Adapter<CreditsAdapter.ViewHolder>() {

    inner class ViewHolder(
            val binding: CreditsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(credit: Credit) {
            binding.nameTv.text = credit.name
            binding.dateTv.text = sdf.format(credit.endDate.time)
            binding.cashTv.text = credit.cash.toString()
        }
    }

    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

    var data: List<Credit> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CreditsListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val credit = data[position]
        holder.bind(credit)
    }

    override fun getItemCount(): Int = data.size
}