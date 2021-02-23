package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityUserBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.user_fragments.HomeFragment
import com.example.worldskills.ui.user_fragments.ProfileFragment

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    lateinit var token: String
    lateinit var password: String

    fun logoutUser() {
        val res = BankApi.logout(token)
        if (res)
            Handler(Looper.getMainLooper()).post {
                finish()
            }
    }

    fun loadUserInfo() {
        val userInfo = BankApi.getUser(token)!!

        Handler(Looper.getMainLooper()).post {
            binding.userInfoTv.text = "${userInfo.firstName} ${userInfo.patronymic}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        token = intent.getStringExtra("token")!!
        password = intent.getStringExtra("password")!!

        Thread { loadUserInfo() }.start()

        loadFragment(HomeFragment())

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 1)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            else if (supportFragmentManager.backStackEntryCount == 0)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }

        binding.userBnv.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when(item.itemId) {
                R.id.home_btv_item -> HomeFragment()

                else -> return@setOnNavigationItemSelectedListener false
            }

            loadFragment(fragment)

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_actionbar_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            supportFragmentManager.popBackStack()
            true
        }

        R.id.profile_item -> {
            addFragment(ProfileFragment())
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commit()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }
}
