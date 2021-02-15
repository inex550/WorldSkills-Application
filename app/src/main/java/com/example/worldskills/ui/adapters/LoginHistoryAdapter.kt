package com.example.worldskills.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.worldskills.databinding.LoginHistoryListItemBinding
import com.example.worldskills.models.LastLogin
import java.text.SimpleDateFormat
import java.util.*

class LoginHistoryAdapter: RecyclerView.Adapter<LoginHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(
            val binding: LoginHistoryListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(lastLogin: LastLogin) {
            binding.dateTv.text = sdfDate.format(lastLogin.date.time)
            binding.timeTv.text = sdfTime.format(lastLogin.time.time)
        }
    }

    private val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    private val sdfTime = SimpleDateFormat("hh.mm", Locale.ENGLISH)

    var data = listOf<LastLogin>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LoginHistoryListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lastLogin = data[position]
        holder.bind(lastLogin)
    }

    override fun getItemCount(): Int = data.size
}