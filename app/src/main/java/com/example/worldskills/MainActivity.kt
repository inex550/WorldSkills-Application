package com.example.worldskills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.worldskills.databinding.ActivityMainBinding
import com.example.worldskills.network.CbrApi
import com.example.worldskills.ui.BankomatsMapsActivity
import com.example.worldskills.ui.ValutesActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    fun loadUsdEur() {
        val valutes = CbrApi.loadValutes(Calendar.getInstance())

        var usd = "..."
        var eur = "..."

        for (valute in valutes) {
            if (valute.charCode == "USD")
                usd = valute.value.toString()

            if (valute.charCode == "EUR")
                eur = valute.value.toString()
        }

        Handler(Looper.getMainLooper()).post {
            binding.usdCourse.text = usd
            binding.eurCourse.text = eur
        }
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread {
            loadUsdEur()
        }.start()

        binding.toBranchesBtn.setOnClickListener {
            val intent = Intent(this, BankomatsMapsActivity::class.java)
            startActivity(intent)
        }

        binding.toExchangesBtn.setOnClickListener {
            val intent = Intent(this, ValutesActivity::class.java)
            startActivity(intent)
        }

        binding.signInBtn.setOnClickListener {

        }
    }

    companion object {
        const val TAG = "MainActivityTAG"
    }
}