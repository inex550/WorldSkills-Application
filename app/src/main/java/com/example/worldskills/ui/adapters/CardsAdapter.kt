package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.R
import com.example.worldskills.databinding.CardsListItemBinding
import com.example.worldskills.models.Card

class CardsAdapter: RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: CardsListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card) {
            binding.nameTv.text = card.name
            binding.numTv.text = card.num
            binding.cashTv.text = card.cash.toString()

            binding.imageIv.setImageResource(when (card.type) {
                "mir" -> R.drawable.mir
                "visa" -> R.drawable.visa
                "master_card" -> R.drawable.master_card
                else -> 0
            })
        }
    }

    var data: List<Card> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardsListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = data[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = data.size
}