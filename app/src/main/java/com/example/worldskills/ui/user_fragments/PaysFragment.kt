package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.FragmentOperationsBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.OperationsAdapter

class PaysFragment: Fragment() {

    private var _binding: FragmentOperationsBinding? = null
    private val binding: FragmentOperationsBinding get() = _binding!!

    private val adapter = OperationsAdapter()

    private fun loadLastPays() {
        val token = (requireActivity() as UserActivity).token

        val pays = BankApi.loadPaysHistory(token)

        Handler(Looper.getMainLooper()).post {
            if (pays == null) {
                Toast.makeText(requireContext(), "При загрузке данных что-то пошло не так :|", Toast.LENGTH_SHORT).show()
                return@post
            }

            adapter.data = pays.reversed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOperationsBinding.inflate(inflater, container, false)

        (requireActivity() as UserActivity).showSearch(true)

        binding.operationsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.operationsRv.adapter = adapter

        Thread { loadLastPays() }.start()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}