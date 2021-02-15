package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.FragmentLoginHistoryBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.LoginHistoryAdapter

class LoginHistoryFragment: Fragment() {

    private var _binding: FragmentLoginHistoryBinding? = null

    private val binding: FragmentLoginHistoryBinding
        get() = _binding!!


    lateinit var adapter: LoginHistoryAdapter

    fun loadUserLoginHistory() {
        val token = (requireActivity() as UserActivity).token

        val lastLogins = BankApi.lastLogin(token)

        Handler(Looper.getMainLooper()).post {
            adapter.data = lastLogins!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginHistoryBinding.inflate(inflater, container, false)

        adapter = LoginHistoryAdapter()

        binding.historyRv.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRv.adapter = adapter

        Thread { loadUserLoginHistory() }.start()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}