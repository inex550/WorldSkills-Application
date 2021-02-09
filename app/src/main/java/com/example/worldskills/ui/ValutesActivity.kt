package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.ActivityValutesBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.network.CbrApi
import com.example.worldskills.ui.adapters.ValuteAdapter
import java.text.SimpleDateFormat
import java.util.*

class ValutesActivity : AppCompatActivity() {

    lateinit var binding: ActivityValutesBinding

    val time = Calendar.getInstance()

    lateinit var adapter: ValuteAdapter

    fun loadValutes() {
        val valutes = CbrApi.loadValutes(time)
        val cfcs = BankApi.loadValutes()

        adapter.valutes = valutes
        adapter.cfcs = cfcs

        Handler(Looper.getMainLooper()).post {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValutesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ValuteAdapter()

        binding.currentDateTv.text = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(time.time)

        binding.valutesListRv.layoutManager = LinearLayoutManager(this)
        binding.valutesListRv.adapter = adapter

        Thread {
            loadValutes()
        }.start()
    }
}