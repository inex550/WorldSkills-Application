package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.R
import com.example.worldskills.databinding.SelectCardListItemBinding
import com.example.worldskills.models.Card

class SelectCardListAdapter(
        private val listener: OnCardsItemClickListener
): RecyclerView.Adapter<SelectCardListAdapter.ViewHolder>() {

    inner class ViewHolder(
            private val binding: SelectCardListItemBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cardRl.setOnClickListener(this)
        }

        fun bind(card: Card) {
            binding.cardNameTv.text = card.name
            binding.cardNumTv.text = "${card.num.take(4)}****${card.num.takeLast(4)}"
            binding.cardCashTv.text = card.cash.toString()
            binding.cardTypeIv.setImageResource(when (card.type) {
                "mir" -> R.drawable.mir
                "visa" -> R.drawable.visa
                "master_card" -> R.drawable.master_card
                else -> 0
            })
        }

        override fun onClick(view: View?) {
            val card = data[adapterPosition]
            listener.onCardsItemClick(card)
        }
    }

    interface OnCardsItemClickListener {
        fun onCardsItemClick(card: Card)
    }

    var data: List<Card> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SelectCardListItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = data[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = data.size
}