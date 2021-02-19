package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.FragmentOperationsBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.OperationsAdapter

class OperationsFragment(
        private val opsType: Int,
        private val num: String
): Fragment() {

    var _binding: FragmentOperationsBinding? = null

    val binding: FragmentOperationsBinding
        get() = _binding!!

    lateinit var adapter: OperationsAdapter

    private fun loadOperations(token: String, num: String) {
        val ops = when (opsType) {
            CARD -> BankApi.getCardOperations(token, num)
            CHECK -> BankApi.getCheckOperation(token, num)
            else -> null
        }!!

        Handler(Looper.getMainLooper()).post {
            adapter.data = ops
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOperationsBinding.inflate(inflater, container, false)

        adapter = OperationsAdapter()

        binding.operationsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.operationsRv.adapter = adapter

        val token = (requireActivity() as UserActivity).token
        Thread { loadOperations(token, num) }.start()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CARD = 0
        const val CHECK = 1
    }
}