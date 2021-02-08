package com.example.worldskills.models

import java.time.LocalTime

data class Bankomat(
    val street: String,
    val isWork: Boolean,
    val name: String,
    val workStart: String,
    val workEnd: String,
    val geoLat: Double,
    val geoLng: Double
)