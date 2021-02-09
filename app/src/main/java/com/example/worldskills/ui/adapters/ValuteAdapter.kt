package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.R
import com.example.worldskills.databinding.ValutesListItemBinding
import com.example.worldskills.models.BankValute
import com.example.worldskills.models.Valute

class ValuteAdapter: RecyclerView.Adapter<ValuteAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ValutesListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindWithCfc(valute: Valute, cfc: BankValute) {
            val buyPrice = valute.value!!.times(cfc.buy)
            val cellPrice = valute.value.times(cfc.cell)

            binding.buyTv.text = buyPrice.toString().substring(0, 5)
            binding.cellTv.text = cellPrice.toString().substring(0, 5)

            binding.charCodeTv.text = valute.charCode
            binding.nameValTv.text = valute.name

            binding.buyArrowIv.setImageResource(
                if (valute.value > buyPrice)
                    R.drawable.ic_arrow_up
                else
                    R.drawable.ic_arrow_down
            )
            binding.cellArrowIv.setImageResource(
                if (valute.value > cellPrice)
                    R.drawable.ic_arrow_up
                else
                    R.drawable.ic_arrow_down
            )
        }

        fun bindWithoutCfc(valute: Valute) {
            binding.buyTv.text = "nan"
            binding.cellTv.text = "nan"

            binding.charCodeTv.text = valute.charCode
            binding.nameValTv.text = valute.name
        }
    }

    var valutes: List<Valute> = listOf()
    var cfcs: Map<String, BankValute> = mapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ValutesListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val valute = valutes[position]
        val cfc = cfcs[valute.charCode]

        if (cfc != null) holder.bindWithCfc(valute, cfc)
        else holder.bindWithoutCfc(valute)
    }

    override fun getItemCount(): Int = valutes.size
}