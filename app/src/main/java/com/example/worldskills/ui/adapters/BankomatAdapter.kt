package com.example.worldskills.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.BankomatsListItemBinding
import com.example.worldskills.models.Bankomat
import java.text.SimpleDateFormat
import java.util.*

class BankomatAdapter(
    val listener: OnItemClickListener
): RecyclerView.Adapter<BankomatAdapter.ViewHolder>() {

    val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    inner class ViewHolder (
            private val binding: BankomatsListItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

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

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var data: List<Bankomat> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BankomatsListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bankomat = data[position]
        holder.bind(bankomat)
    }

    override fun getItemCount(): Int = data.size
}