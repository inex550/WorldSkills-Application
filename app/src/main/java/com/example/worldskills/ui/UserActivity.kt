package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityUserBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.user_fragments.HomeFragment

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    lateinit var token: String

    fun logoutUser() {
        val res = BankApi.logout(token)
        if (res)
            Handler(Looper.getMainLooper()).post {
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = intent.getStringExtra("token")!!

        binding.logoutIv.setOnClickListener {
            Thread {
                logoutUser()
            }.start()
        }

        loadFragment(HomeFragment())

        binding.userBnv.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when(item.itemId) {
                R.id.home_btv_item -> HomeFragment()

                else -> return@setOnNavigationItemSelectedListener false
            }

            loadFragment(fragment)

            return@setOnNavigationItemSelectedListener true
        }
    }

    fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commit()
    }
}