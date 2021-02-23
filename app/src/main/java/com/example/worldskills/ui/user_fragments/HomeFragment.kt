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
import com.example.worldskills.models.Card
import com.example.worldskills.models.Check
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.CardsAdapter
import com.example.worldskills.ui.adapters.ChecksAdapter
import com.example.worldskills.ui.adapters.CreditsAdapter

class HomeFragment : Fragment(), CardsAdapter.OnCardClickListener, ChecksAdapter.OnItemClickListener {

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

        cardsAdapter = CardsAdapter(this)
        checksAdapter = ChecksAdapter(this)
        creditsAdapter = CreditsAdapter()

        binding.cardsListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.checksListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.creditsListRv.layoutManager = LinearLayoutManager(requireContext())

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

    override fun onCardClick(card: Card) {
        val cards = cardsAdapter.data.filter { listCard -> listCard != card }
        (requireActivity() as UserActivity).addFragment(CardFragment(card, cards))
    }

    override fun onItemClick(check: Check) {
        val checks = checksAdapter.data.filter { listCheck -> listCheck != check }
        (requireActivity() as UserActivity).addFragment(CheckFragment(check, checks))
    }
}