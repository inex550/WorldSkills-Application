package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.worldskills.R
import com.example.worldskills.databinding.ActivityBankomatsBinding

class BankomatsActivity : AppCompatActivity() {

    lateinit var binding: ActivityBankomatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankomatsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}