package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.NetworkOnMainThreadException
import com.example.worldskills.databinding.ActivityUserBinding
import com.example.worldskills.models.UserSecret
import com.example.worldskills.network.BankApi
import com.example.worldskills.network.NetworkService

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    lateinit var userSecret: UserSecret

    fun logoutUser() {
        val res = BankApi.logout(userSecret)
        if (res)
            Handler(Looper.getMainLooper()).post {
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userSecret = intent.getSerializableExtra("user_secret") as UserSecret

        binding.logoutIv.setOnClickListener {
            Thread {
                logoutUser()
            }.start()
        }
    }
}