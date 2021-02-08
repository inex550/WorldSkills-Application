package com.example.worldskills.ui.adapters

import android.graphics.Color
import android.text.format.Time
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.BankomatsListItemBinding
import com.example.worldskills.models.Bankomat
import java.text.SimpleDateFormat
import java.util.*

class BankomatAdapter: RecyclerView.Adapter<BankomatAdapter.ViewHolder>() {

    class ViewHolder (
            private val binding: BankomatsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(bankomat: Bankomat) {
            binding.streetTv.text = bankomat.street
            binding.typeTv.text = bankomat.name
            binding.startTimeTv.text = sdf.format(bankomat.workStart.time)
            binding.endTimeTv.text = sdf.format(bankomat.workEnd.time)

            if (bankomat.isWork) {
                binding.isWorkTv.text = "Работает"
                binding.isWorkTv.setTextColor(Color.GREEN)
            } else {
                binding.isWorkTv.text = "Закрыто"
                binding.isWorkTv.setTextColor(Color.RED)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = BankomatsListItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }

            val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        }
    }

    var data: List<Bankomat> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankomat = data[position]
        holder.bind(bankomat)
    }

    override fun getItemCount(): Int = data.size
}