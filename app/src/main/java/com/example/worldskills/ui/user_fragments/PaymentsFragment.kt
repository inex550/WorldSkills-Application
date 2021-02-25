package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worldskills.databinding.FragmentPaymentsBinding
import com.example.worldskills.models.Payment
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity

class PaymentsFragment: Fragment() {

    private var _binding: FragmentPaymentsBinding? = null
    private val binding: FragmentPaymentsBinding get() = _binding!!

    private var payments: List<Payment>? = null

    fun loadPayments() {
        val token = (requireActivity() as UserActivity).token

        payments = BankApi.loadCategory(token)

        Handler(Looper.getMainLooper()).post {
            if (payments == null) {
                Toast.makeText(requireContext(), "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show()
                return@post
            }

            val data = payments!!.map { payment -> payment.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
            binding.paymentsListRv.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)

        Thread { loadPayments() }.start()
        
        binding.paymentsListRv.setOnItemClickListener { _, _, position, _ ->
            val payment = payments!![position]
            (requireActivity() as UserActivity).loadFragment(PaymentFragment(payment))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}