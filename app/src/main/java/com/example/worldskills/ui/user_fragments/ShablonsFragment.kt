package com.example.worldskills.ui.user_fragments

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldskills.database.Crud
import com.example.worldskills.database.DbHelper
import com.example.worldskills.database.ShablonEntry
import com.example.worldskills.databinding.FragmentShablonBinding
import com.example.worldskills.ui.SwipeController
import com.example.worldskills.ui.adapters.ShablonsAdapter

class ShablonsFragment: Fragment() {

    private var _binding: FragmentShablonBinding? = null
    private val binding: FragmentShablonBinding get() = _binding!!

    private lateinit var dbHelper: DbHelper

    private val adapter = ShablonsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShablonBinding.inflate(inflater, container, false)

        dbHelper = DbHelper(requireContext())
        //addSimpleShablons(10)

        val shablons = Crud.selectAllShablons(dbHelper)
        adapter.data = shablons

        binding.shablonsListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.shablonsListRv.adapter = adapter

        val swipeController = SwipeController()

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.shablonsListRv)

        return binding.root
    }

    private fun addSimpleShablons(count: Int) {
        val db = dbHelper.writableDatabase

        for (i in 0 until count) {
            val values = ContentValues().apply {
                put("name", "Шаблон $i")
                put("sum", 100)
            }

            db.insert(ShablonEntry.TABLE_NAME, null, values)
        }

        db.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}