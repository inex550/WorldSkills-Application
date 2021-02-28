package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.ShablonsListItemBinding
import com.example.worldskills.models.Shablon

class ShablonsAdapter(
        private val controller: ShablonButtonsController
): RecyclerView.Adapter<ShablonsAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val binding: ShablonsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(shablon: Shablon) {
            binding.nameTv.text = shablon.name

            binding.changeItemTv.setOnClickListener {
                controller.onChangeClicked(adapterPosition)
            }

            binding.removeItemTv.setOnClickListener {
                controller.onRemoveClicked(adapterPosition)
            }
        }

        fun showButtons(show: Boolean) {
            binding.btnsLl.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    abstract class ShablonButtonsController {
        abstract fun onChangeClicked(position: Int)

        abstract fun onRemoveClicked(position: Int)
    }

    var data = mutableListOf<Shablon>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShablonsListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shablon = data[position]
        holder.bind(shablon)
    }

    override fun getItemCount(): Int = data.size
}