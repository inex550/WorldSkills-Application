package com.example.worldskills.ui.user_fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private fun editProfileData(type: Int, data: String) {
        if (data.isEmpty()) {
            cdd.setError("Поле не заполнено")
            return
        }

        Handler(Looper.getMainLooper()).post {
            val token = (requireActivity() as UserActivity).token

            if (type == ChangeDataDialog.LOGIN && BankApi.editLogin(token, data))
                cdd.dismiss()
            else if (type == ChangeDataDialog.PASSWORD && BankApi.editPassword(token, data))
                cdd.dismiss()
        }
    }

    private fun logout() {
        val token = (requireActivity() as UserActivity).token

        if (BankApi.logout(token))
            Handler(Looper.getMainLooper()).post {
                (requireActivity() as UserActivity).finish()
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.changePasswordRl.setOnClickListener {
            cdd = ChangeDataDialog(this, ChangeDataDialog.PASSWORD)
            cdd.show(requireActivity().supportFragmentManager, "dialog")
        }

        binding.changeLoginRl.setOnClickListener {
            cdd = ChangeDataDialog(this, ChangeDataDialog.LOGIN)
            cdd.show(requireActivity().supportFragmentManager, "dialog")
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


    override fun onChangeDataClick(type: Int, data: String) {
        Thread {
            editProfileData(type, data)
        }.start()
    }
}