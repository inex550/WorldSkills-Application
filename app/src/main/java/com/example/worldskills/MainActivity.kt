package com.example.worldskills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.worldskills.databinding.ActivityMainBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.network.CbrApi
import com.example.worldskills.ui.BankomatsMapsActivity
import com.example.worldskills.ui.LoginDialogFragment
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.ValutesActivity
import java.util.*

class MainActivity : AppCompatActivity(), LoginDialogFragment.OnSignInClickListener {

    private fun loadUsdEur() {
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

    private fun loginQuery(login: String, password: String) {
        val token = BankApi.login(login, password)

        if (token == null)
            Handler(Looper.getMainLooper()).post {
                loginDialog.setError("Не правильное имя пользователя или пароль")
            }
        else {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)

            loginDialog.dismiss()
        }
    }

    lateinit var loginDialog: LoginDialogFragment

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
            loginDialog = LoginDialogFragment(this)
            loginDialog.show(supportFragmentManager, "login_dialog")
        }
    }

    companion object {
        const val TAG = "MainActivityTAG"
    }

    override fun onSignInClick(login: String, password: String) {
        if (login.isEmpty() || password.isEmpty())
            loginDialog.setError("Не все поля заполнены")
        else Thread {
            loginQuery(login, password)
        }.start()
    }
}