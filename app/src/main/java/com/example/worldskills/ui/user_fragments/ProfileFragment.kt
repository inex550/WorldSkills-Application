package com.example.worldskills.ui.user_fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.worldskills.databinding.FragmentProfileBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity

class ProfileFragment: Fragment(), ChangeDataDialog.OnChangeDataClickListener {

    var _binding: FragmentProfileBinding? = null

    val binding: FragmentProfileBinding get() = _binding!!

    lateinit var cdd: ChangeDataDialog

    private fun editLogin(token: String, login: String) {
        val isEdit = BankApi.editLogin(token, login)

        Handler(Looper.getMainLooper()).post {
            if (isEdit) cdd.dismiss()
            else cdd.setError("Ошибка :(")
        }
    }

    private fun editPassword(token: String, password: String) {
        val isEdit = BankApi.editPassword(token, password)

        Handler(Looper.getMainLooper()).post {
            if (isEdit) cdd.dismiss()
            else cdd.setError("Ошибка :(")
        }
    }

    private fun logout() {
        val token = (requireActivity() as UserActivity).token

        if (BankApi.logout(token))
            Handler(Looper.getMainLooper()).post {
                (requireActivity() as UserActivity).finish()
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        (requireActivity() as UserActivity).showSearch(false)

        binding.changePasswordRl.setOnClickListener {
            cdd = ChangeDataDialog(this, "Изменение пароля", "Пароль", "Изменить", "Введите новый пароль", InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            cdd.show(requireActivity().supportFragmentManager, "password_dialog")
        }

        binding.changeLoginRl.setOnClickListener {
            cdd = ChangeDataDialog(this, "Изменение логина", "Логин", "Изменить", "Введите новый логин")
            cdd.show(requireActivity().supportFragmentManager, "login_dialog")
        }

        binding.logIoHistory.setOnClickListener {
            (requireActivity() as UserActivity).addFragment(LoginHistoryFragment())
        }

        binding.applicationInfoRl.setOnClickListener {
            (requireActivity() as UserActivity).addFragment(AppInfoFragment())
        }

        binding.exitRl.setOnClickListener {
            Thread { logout() }.start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onChangeDataClick(data: String, tag: String?) {
        if (data.isEmpty()) {
            cdd.setError("Данные не введены")
            return
        }

        val token = (requireActivity() as UserActivity).token

        Thread {
            if (tag == "login_dialog") editLogin(token, data)
            else if (tag == "password_dialog") editPassword(token, data)
        }.start()
    }
}