package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.worldskills.databinding.ActivityValutesBinding

class ValutesActivity : AppCompatActivity() {

    lateinit var binding: ActivityValutesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValutesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}