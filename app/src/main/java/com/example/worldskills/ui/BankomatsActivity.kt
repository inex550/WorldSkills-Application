package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityBankomatsBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.adapters.BankomatAdapter

class BankomatsActivity : AppCompatActivity() {

    lateinit var binding: ActivityBankomatsBinding

    lateinit var adapter: BankomatAdapter

    fun loadBankomats() {
        val bankomats = BankApi.loadBankomats()

        Handler(Looper.getMainLooper()).post {
            adapter.data = bankomats
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankomatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BankomatAdapter()

        binding.bankomatsRv.layoutManager = LinearLayoutManager(this)
        binding.bankomatsRv.adapter = adapter

        Thread {
            loadBankomats()
        }.start()
    }
}