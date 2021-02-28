package com.example.worldskills.ui.user_fragments

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.example.worldskills.database.Crud
import com.example.worldskills.database.DbHelper
import com.example.worldskills.database.ShablonEntry
import com.example.worldskills.databinding.FragmentShablonSaveBinding
import com.example.worldskills.models.Shablon

class ShablonSaveFragment(
        private val shablon: Shablon
): Fragment() {

    private var _binding: FragmentShablonSaveBinding? = null
    private val binding: FragmentShablonSaveBinding get() = _binding!!

    private lateinit var dbHelper: DbHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShablonSaveBinding.inflate(inflater, container, false)

        dbHelper = DbHelper(requireContext())

        binding.sumEt.setText(shablon.sum.toString())

        binding.saveBtn.setOnClickListener {
            val sumStr = binding.sumEt.text.toString()

            if (sumStr.isEmpty()) {
                Toast.makeText(requireContext(), "Сумма не введена", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!sumStr.isDigitsOnly()) {
                Toast.makeText(requireContext(), "Сумма введена не корректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sum = sumStr.toInt()
            shablon.sum = sum

            Crud.updateShablon(dbHelper, shablon)

            requireFragmentManager().popBackStack()
        }

        return binding.root
    }
}