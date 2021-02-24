package com.example.worldskills.models

data class Card(
    var name: String,
    val num: String,
    val type: String,
    var cash: Int,
    var blocked: Boolean
)