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
import com.example.worldskills.databinding.FragmentSelectForAddBinding
import com.example.worldskills.models.Card
import com.example.worldskills.models.Check
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.ChecksAdapter
import com.example.worldskills.ui.adapters.SelectCardListAdapter

class SelectForAddCashFragment(
        private val selectedCard: Card,
        private val cards: List<Card>
): Fragment(), ChecksAdapter.OnItemClickListener, SelectCardListAdapter.OnCardsItemClickListener {

    private var _binding: FragmentSelectForAddBinding? = null
    private val binding: FragmentSelectForAddBinding get() = _binding!!

    private lateinit var checks: List<Check>

    private val cardsAdapter = SelectCardListAdapter(this)
    private val checksAdapter = ChecksAdapter(this)

    private fun loadChecks() {
        val token = (requireActivity() as UserActivity).token
        checks = BankApi.getChecks(token)!!

        Handler(Looper.getMainLooper()).post {
            checksAdapter.data = checks
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectForAddBinding.inflate(inflater, container, false)

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

        if (cardsAdapter.data.isEmpty())
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
        (requireActivity() as UserActivity).addFragment(SendCashFragment(card, selectedCard))
    }
}