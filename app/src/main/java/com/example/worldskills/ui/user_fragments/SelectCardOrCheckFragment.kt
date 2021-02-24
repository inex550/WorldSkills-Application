package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.R
import com.example.worldskills.databinding.FragmentSelectCardOrCheckBinding
import com.example.worldskills.models.Card
import com.example.worldskills.models.Check
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.ChecksAdapter
import com.example.worldskills.ui.adapters.SelectCardListAdapter

class SelectCardOrCheckFragment(
        private val currentCard: Card
): Fragment(), ChecksAdapter.OnItemClickListener, SelectCardListAdapter.OnCardsItemClickListener {

    private var _binding: FragmentSelectCardOrCheckBinding? = null
    private val binding: FragmentSelectCardOrCheckBinding get() = _binding!!

    private lateinit var cards: List<Card>
    private lateinit var checks: List<Check>

    private lateinit var cardsAdapter: SelectCardListAdapter
    private lateinit var checksAdapter: ChecksAdapter

    private fun loadCards() {
        val token = (requireActivity() as UserActivity).token
        cards = BankApi.getCards(token)!!
        cards = cards.filter { card -> !card.blocked && card.num != currentCard.num }

        Handler(Looper.getMainLooper()).post {
            cardsAdapter.data = cards
        }
    }

    private fun loadChecks() {
        val token = (requireActivity() as UserActivity).token
        checks = BankApi.getChecks(token)!!

        Handler(Looper.getMainLooper()).post {
            checksAdapter.data = checks
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectCardOrCheckBinding.inflate(inflater, container, false)

        cardsAdapter = SelectCardListAdapter(this)
        checksAdapter = ChecksAdapter(this)

        binding.itemsListRv.layoutManager = LinearLayoutManager(requireContext())

        selectCards()

        binding.selectToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.check_cards_btn -> selectCards()
                R.id.check_checks_btn -> selectChecks()
            }
        }

        return binding.root
    }

    private fun selectCards() {
        binding.itemsListRv.adapter = cardsAdapter

        if (!this::cards.isInitialized) {
            Thread { loadCards() }.start()
            return
        }

        if (cardsAdapter.data.isEmpty() && cards.isNotEmpty())
            cardsAdapter.data = cards
    }

    private fun selectChecks() {
        binding.itemsListRv.adapter = checksAdapter

        if (!this::checks.isInitialized) {
            Thread { loadChecks() }.start()
            return
        }

        if (checksAdapter.data.isEmpty() && checks.isNotEmpty())
            checksAdapter.data = checks
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(check: Check) {}

    override fun onCardsItemClick(card: Card) {
        (requireActivity() as UserActivity).addFragment(SendCashFragment(card, currentCard))
    }
}