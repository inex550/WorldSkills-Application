package com.example.worldskills.ui.user_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.worldskills.databinding.ChangeDataDialogBinding

class ChangeDataDialog(
        private val listener: OnChangeDataClickListener,
        val type: Int
): DialogFragment() {

    private var _binding: ChangeDataDialogBinding? = null

    private val binding: ChangeDataDialogBinding get() = _binding!!

    fun setError(error: String) {
        binding.errorTv.text = error
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ChangeDataDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val title = when(type) {
            LOGIN -> "Изменение логина"
            PASSWORD -> "Изменение пароля"
            else -> "nan"
        }

        val hint = when(type) {
            LOGIN -> "Логин"
            PASSWORD -> "Пароль"
            else -> "nan"
        }

        binding.loginEt.hint = hint

        val dialog = AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(binding.root)
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Изменить", null)
                .show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            val data = binding.loginEt.text.toString()
            listener.onChangeDataClick(type, data)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnChangeDataClickListener {
        fun onChangeDataClick(type: Int, data: String)
    }

    companion object {
        const val LOGIN = 1
        const val PASSWORD = 2
    }
}