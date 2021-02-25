package com.example.worldskills.ui.user_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.databinding.FragmentSelectForSendBinding
import com.example.worldskills.models.Card
import com.example.worldskills.ui.UserActivity
import com.example.worldskills.ui.adapters.SelectCardListAdapter

class SelectForSendCashFragment(
        private val selectedCard: Card,
        private val cards: List<Card>
): Fragment(), SelectCardListAdapter.OnCardsItemClickListener {

    private var _binding: FragmentSelectForSendBinding? = null
    private val binding: FragmentSelectForSendBinding get() = _binding!!

    private val adapter = SelectCardListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectForSendBinding.inflate(inflater, container, false)

        binding.cardsListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.cardsListRv.adapter = adapter

        adapter.data = cards

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCardsItemClick(card: Card) {
        (requireActivity() as UserActivity).addFragment(SendCashFragment(selectedCard, card))
    }
}