package com.example.worldskills.ui.user_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.worldskills.databinding.FragmentCheckBinding
import com.example.worldskills.models.Check
import com.example.worldskills.ui.UserActivity
import kotlin.math.abs

class CheckFragment(
        val check: Check,
        val otherChecks: List<Check>
): Fragment() {

    private var _binding: FragmentCheckBinding? = null

    val binding: FragmentCheckBinding
        get() = _binding!!

    var x1 = 0f
    var x2 = 0f

    var currentCheckPosition = -1
    var currentCheck = check

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)

        setCheckData(currentCheck)

        binding.swipeRl.setOnTouchListener { v, event ->
            return@setOnTouchListener when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    if (abs(x1 - x2) > 150) {
                        val checkPos = currentCheckPosition + if (x1 > x2) 1 else -1

                        if (checkPos == -1) {
                            currentCheck = check
                            currentCheckPosition = -1

                            setCheckData(currentCheck)
                        }

                        val check = otherChecks.getOrNull(checkPos)
                        if (check != null) {
                            currentCheck = check
                            currentCheckPosition = checkPos

                            setCheckData(currentCheck)
                        }
                    }
                    true
                }
                else -> false
            }
        }

        binding.operationHistoryTv.setOnClickListener {
            (requireActivity() as UserActivity).addFragment(OperationsFragment(OperationsFragment.CHECK, currentCheck.num))
        }

        return binding.root
    }

    private fun setCheckData(check: Check) {
        binding.checkNumTv.text = check.num
        binding.checkCashTv.text = check.cash.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}