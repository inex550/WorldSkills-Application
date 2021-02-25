package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.worldskills.databinding.FragmentSendCashBinding
import com.example.worldskills.models.Card
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity

class SendCashFragment(
        private val sourceCard: Card,
        private val destCard: Card
): Fragment(), ChangeDataDialog.OnChangeDataClickListener {

    private var _binding: FragmentSendCashBinding? = null
    private val binding: FragmentSendCashBinding get() = _binding!!

    private lateinit var cddPassword: ChangeDataDialog

    private var sum: Int? = null

    private fun sendCash(sum: Int) {
        val token = (requireActivity() as UserActivity).token

        val code = BankApi.refill(token, sourceCard.num, destCard.num, sum)

        Handler(Looper.getMainLooper()).post {
            when (code) {
                200 -> {
                    Toast.makeText(requireContext(), "Операция выполнена успешно!", Toast.LENGTH_SHORT).show()

                    sourceCard.cash -= sum
                    destCard.cash += sum

                    updateUI()
                    cddPassword.dismiss()
                }
                400 -> Toast.makeText(requireContext(), "На карте не достаточно средств", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(requireContext(), "Что-то пошло не так :(", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSendCashBinding.inflate(inflater, container, false)

        updateUI()

        binding.sendCashBtn.setOnClickListener {
            val sumStr = binding.enterSumEt.text.toString()

            if (sumStr.isEmpty()) {
                Toast.makeText(requireContext(), "Вы ничего не ввели", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!sumStr.isDigitsOnly()) {
                Toast.makeText(requireContext(), "Сумма введена некорректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sum = sumStr.toInt()

            cddPassword = ChangeDataDialog(this, "Подтверждение", "Пароль", "Подтвердить", null, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            cddPassword.show(requireFragmentManager(), "password_dialog")
        }

        return binding.root
    }

    private fun updateUI() {
        binding.sourceCashTv.text = sourceCard.cash.toString()
        binding.sourceNumTv.text = "${sourceCard.num.take(4)}****${sourceCard.num.takeLast(4)}"

        binding.distCashTv.text = destCard.cash.toString()
        binding.distNumTv.text = "${destCard.num.take(4)}****${destCard.num.takeLast(4)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChangeDataClick(data: String, tag: String?) {
        if (data.isEmpty()) {
            cddPassword.setError("Ничего не введено")
            return
        }

        if (data != (requireActivity() as UserActivity).password) {
            cddPassword.setError("Не правильный пароль")
            return
        }

        binding.enterSumEt.setText("")

        Thread { sendCash(sum!!) }.start()
    }
}