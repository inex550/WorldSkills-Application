package com.example.worldskills.ui.user_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.worldskills.databinding.ChangeDataDialogBinding

class ChangeDataDialog(
        private val listener: OnChangeDataClickListener,
        private val title: String,
        private val hint: String,
        private val posBtnHint: String,
        private val message: String? = "",
        private val inputType: Int = InputType.TYPE_CLASS_TEXT
): DialogFragment() {

    private var _binding: ChangeDataDialogBinding? = null

    private val binding: ChangeDataDialogBinding get() = _binding!!

    fun setError(error: String) {
        binding.errorTv.text = error
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ChangeDataDialogBinding.inflate(LayoutInflater.from(requireContext()))

        binding.loginEt.inputType = inputType
        binding.loginEt.hint = hint

        val dialog = AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setView(binding.root)
                .setNegativeButton(posBtnHint, null)
                .setPositiveButton("Отмена", null)
                .show()

        val btn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        btn.setOnClickListener {
            val data = binding.loginEt.text.toString()
            listener.onChangeDataClick(data, tag)
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnChangeDataClickListener {
        fun onChangeDataClick(data: String, tag: String?)
    }

    companion object {
        const val LOGIN = 1
        const val PASSWORD = 2
    }
}