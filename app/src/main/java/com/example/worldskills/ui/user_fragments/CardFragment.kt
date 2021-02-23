package com.example.worldskills.ui.user_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worldskills.R
import com.example.worldskills.databinding.FragmentCardBinding
import com.example.worldskills.models.Card
import kotlin.math.abs

class CardFragment(
    val card: Card,
    val cards: List<Card>
): Fragment() {

    var _binding: FragmentCardBinding? = null

    val binding: FragmentCardBinding get() = _binding!!

    var x1 = 0f
    var x2 = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        setCardData(card)

        binding.swipeRl.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) return@setOnTouchListener binding.swipeRl.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_DOWN -> x1 = event.x
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    Toast.makeText(requireContext(), "Длина свайпа - ${abs(x1 - x2)}", Toast.LENGTH_SHORT).show()
                }
            }

            return@setOnTouchListener binding.swipeRl.onTouchEvent(event)
        }

        return binding.root
    }

    private fun setCardData(card: Card) {
        binding.cardNameTv.text = card.name
        binding.cardNumTv.text = "${card.num.take(4)}****${card.num.takeLast(4)}"
        binding.cardCashTv.text = card.cash.toString()
        binding.cardTypeIv.setImageResource(when (card.type) {
            "mir" -> R.drawable.mir
            "visa" -> R.drawable.visa
            "master_card" -> R.drawable.master_card
            else -> 0
        })
    }
}