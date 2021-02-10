package com.example.worldskills.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.worldskills.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}