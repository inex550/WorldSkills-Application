package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.FragmentHomeBinding
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.CardsAdapter
import com.example.worldskills.ui.adapters.ChecksAdapter
import com.example.worldskills.ui.adapters.CreditsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding: FragmentHomeBinding
        get() = _binding!!

    lateinit var token: String

    lateinit var cardsAdapter: CardsAdapter
    lateinit var checksAdapter: ChecksAdapter
    lateinit var creditsAdapter: CreditsAdapter

    private fun loadCards(token: String) {
        val cards = BankApi.getCards(token) ?: return

        Handler(Looper.getMainLooper()).post {
            cardsAdapter.data = cards
        }
    }

    private fun loadChecks(token: String) {
        val checks = BankApi.getChecks(token) ?: return

        Handler(Looper.getMainLooper()).post {
            checksAdapter.data = checks
        }
    }

    private fun loadCredits(token: String) {
        val credits = BankApi.getCredits(token) ?: return

        Handler(Looper.getMainLooper()).post {
            creditsAdapter.data = credits
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        token = (requireActivity() as UserActivity).token

        cardsAdapter = CardsAdapter()
        checksAdapter = ChecksAdapter()
        creditsAdapter = CreditsAdapter()

        binding.cardsListRv.adapter = cardsAdapter
        binding.checksListRv.adapter = checksAdapter
        binding.creditsListRv.adapter = creditsAdapter

        Thread { loadCards(token) }.start()
        Thread { loadChecks(token) }.start()
        Thread { loadCredits(token) }.start()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}