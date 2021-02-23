package com.example.worldskills.models

data class Card(
    var name: String,
    val num: String,
    val type: String,
    val cash: Int,
    var blocked: Boolean
)