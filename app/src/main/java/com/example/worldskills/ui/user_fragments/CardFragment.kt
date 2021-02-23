package com.example.worldskills.ui.user_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.worldskills.R
import com.example.worldskills.databinding.FragmentCardBinding
import com.example.worldskills.models.Card
import com.example.worldskills.network.BankApi
import com.example.worldskills.ui.UserActivity
import kotlin.math.abs

class CardFragment(
        private val card: Card,
        private val cards: List<Card>
): Fragment(), ChangeDataDialog.OnChangeDataClickListener {

    var _binding: FragmentCardBinding? = null

    val binding: FragmentCardBinding get() = _binding!!

    var x1 = 0f
    var x2 = 0f

    var cardPosition = -1
    var currentCard = card

    fun block(token: String, card: Card) {
        val blocked = BankApi.block(token, card.num)

        Handler(Looper.getMainLooper()).post {
            if (blocked) {
                uiBlock(true)
                currentCard.blocked = true
            }
            cdd.dismiss()
        }
    }

    private fun renameCard(token: String, cardNum: String, newName: String) {
        val nameChanged = BankApi.changeCardName(token, cardNum, newName)

        Handler(Looper.getMainLooper()).post {
            if (nameChanged) {
                currentCard.name = newName
                setCardData(currentCard)
            } else
                Toast.makeText(requireContext(), "Ошибка при изменении данных :(", Toast.LENGTH_SHORT).show()

            cdd.dismiss()
        }
    }

    lateinit var cdd: ChangeDataDialog


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        setCardData(currentCard)
        if (currentCard.blocked) uiBlock(true)

        binding.swipeRl.setOnTouchListener { v, event ->
            return@setOnTouchListener when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    if (abs(x1 - x2) > 400) {
                        val cardI = cardPosition + if (x1 > x2) 1 else -1

                        if (cardI == -1) {
                            setCardData(card)
                            currentCard = card
                            cardPosition = cardI
                        }

                        val card = cards.getOrNull(cardI)
                        if (card != null) {
                            setCardData(card)
                            cardPosition = cardI
                            currentCard = card
                        }

                        uiBlock(currentCard.blocked)
                    }
                    true
                }
                else -> false
            }
        }

        binding.blockTv.setOnClickListener {
            if (currentCard.blocked) {
                Toast.makeText(requireContext(), "Карта уже заблокирована", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                cdd = ChangeDataDialog(this,
                        "Блокировка карты",
                        "Пароль",
                        "Заблокировать",
                        "Вы уверены что хотите заблокировать карту",
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
                cdd.show(requireActivity().supportFragmentManager, "card_block_dialog")
            }
        }

        binding.renameTv.setOnClickListener {
            cdd = ChangeDataDialog(this,
                "Переименование карты",
                "Имя",
                "Изменить",
                "Введите новое название"
            )
            cdd.show(requireActivity().supportFragmentManager, "rename_card_dialog")
        }

        binding.operationHistoryTv.setOnClickListener {
            (requireActivity() as UserActivity).addFragment(OperationsFragment(OperationsFragment.CARD, currentCard.num))
        }

        return binding.root
    }

    private fun uiBlock(block: Boolean) {
        if (block) {
            binding.cardBlockedTv.visibility = View.VISIBLE
            binding.addCashBtn.isEnabled = false
            binding.sendCashBtn.isEnabled = false
            binding.cardBtnLl.visibility = View.INVISIBLE
        } else {
            binding.cardBlockedTv.visibility = View.GONE
            binding.addCashBtn.isEnabled = true
            binding.sendCashBtn.isEnabled = true
            binding.cardBtnLl.visibility = View.VISIBLE
        }
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

    override fun onChangeDataClick(data: String, tag: String?) {
        if (data.isEmpty()) {
            cdd.setError("Ничего не введено")
            return
        }

        val token = (requireActivity() as UserActivity).token

        when (tag) {
            "card_block_dialog" ->
                if (data == (requireActivity() as UserActivity).password)
                    Thread { block(token, currentCard) }.start()
                else
                    cdd.setError("Не правильный пароль")

            "rename_card_dialog" -> Thread { renameCard(token, currentCard.num, data) }.start()
        }
    }
}