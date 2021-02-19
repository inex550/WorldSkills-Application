package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.ChecksListItemBinding
import com.example.worldskills.models.Check

class ChecksAdapter(
    val listener: OnItemClickListener
): RecyclerView.Adapter<ChecksAdapter.ViewHolder>() {

    inner class ViewHolder(
            val binding: ChecksListItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(check: Check) {
            binding.numTv.text = check.num.takeLast(6)
            binding.cashTv.text = check.cash.toString()
        }

        override fun onClick(v: View?) {
            val check = data[adapterPosition]
            listener.onItemClick(check)
        }
    }

    var data: List<Check> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChecksListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val check = data[position]
        holder.bind(check)
    }

    override fun getItemCount(): Int = data.size

    interface OnItemClickListener {
        fun onItemClick(check: Check)
    }
}