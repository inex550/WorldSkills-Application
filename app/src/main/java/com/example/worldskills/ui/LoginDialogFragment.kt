package com.example.worldskills.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.worldskills.MainActivity
import com.example.worldskills.databinding.LoginDialogBinding

class LoginDialogFragment(
        val listener: OnSignInClickListener
): DialogFragment() {

    private var _binding: LoginDialogBinding? = null

    private val binding: LoginDialogBinding get() = _binding!!

    fun setError(error: String) {
        binding.errorTv.text = error
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LoginDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("Авторизация")
            .setMessage("Введите логин и пароль")
            .setView(binding.root)
            .setNegativeButton("Отмена", null)
            .setPositiveButton("Добавить", null)
            .show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            val login = binding.loginEt.text.toString()
            val password = binding.passwordEt.text.toString()
            listener.onSignInClick(login, password)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnSignInClickListener {
        fun onSignInClick(login: String, password: String)
    }
}