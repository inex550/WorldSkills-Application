package com.example.worldskills.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.worldskills.databinding.LoginDialogBinding

class LoginDialogFragment: DialogFragment() {

    private var _binding: LoginDialogBinding? = null

    private val binding: LoginDialogBinding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LoginDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle("Авторизация")
            .setMessage("Введите логин и пароль")
            .setView(binding.root)

        builder.setNegativeButton("Отмена", null)
        builder.setPositiveButton("Добавить") { _, _ ->
            Toast.makeText(requireActivity(), "Login - ${binding.loginEt.text}; Password - ${binding.passwordEt.text}", Toast.LENGTH_SHORT).show()
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}