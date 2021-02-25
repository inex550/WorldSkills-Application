package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.worldskills.databinding.FragmentPaymentBinding
import com.example.worldskills.models.Card
import com.example.worldskills.models.Payment
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.google.android.material.tabs.TabLayout

class PaymentFragment(
        private val payment: Payment
): Fragment(), ChangeDataDialog.OnChangeDataClickListener {

    private var _binding: FragmentPaymentBinding? = null
    private val binding: FragmentPaymentBinding get() = _binding!!

    private lateinit var cddPassword: ChangeDataDialog

    private var cards: List<Card>? = null

    private lateinit var cardNum: String
    private lateinit var checkNum: String
    private var sum: Int = 0

    private fun loadCards() {
        val token = (requireActivity() as UserActivity).token

        cards = BankApi.getCards(token)

        Handler(Looper.getMainLooper()).post {
            if (cards == null) {
                Toast.makeText(requireContext(), "Ошибка при загрузке карт :(", Toast.LENGTH_SHORT).show()
                return@post
            }

            val data = cards!!.map { card -> card.num }
            binding.cardsSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    data
            )
        }
    }

    private fun pay() {
        val token = (requireActivity() as UserActivity).token

        val res = BankApi.pay(token, cardNum, checkNum, sum)

        Handler(Looper.getMainLooper()).post {
            if (res == 404) {
                Toast.makeText(requireContext(), "Счёт указан некорректно", Toast.LENGTH_SHORT).show()
                return@post
            }

            if (res == 400) {
                Toast.makeText(requireContext(), "На карте не достаточно средств", Toast.LENGTH_SHORT).show()
                return@post
            }

            Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show()
            requireFragmentManager().popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)

        binding.cardsSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Карта")
        )

        Thread { loadCards() }.start()

        binding.sendCashBtn.setOnClickListener {
            checkNum = binding.checkNumEt.text.toString()
            val sumStr = binding.sumEt.text.toString()

            if (checkNum.isEmpty()) {
                Toast.makeText(requireContext(), "Номер договора не введён", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sumStr.isEmpty()) {
                Toast.makeText(requireContext(), "Сумма не введена", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!sumStr.isDigitsOnly()) {
                Toast.makeText(requireContext(), "Сумма введена некорректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            cardNum = binding.cardsSpinner.selectedItem.toString()

            sum = sumStr.toInt()

            cddPassword = ChangeDataDialog(this, "Подтверждение", "Пароль", "Подтвердить", null, InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            cddPassword.show(requireFragmentManager(), "password_dialog")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChangeDataClick(data: String, tag: String?) {
        if (data == "") {
            cddPassword.setError("Ничего не введено")
            return
        }

        if (data != (requireActivity() as UserActivity).password) {
            cddPassword.setError("Не верный пароль")
            return
        }

        cddPassword.dismiss()

        Thread { pay() }.start()
    }
}