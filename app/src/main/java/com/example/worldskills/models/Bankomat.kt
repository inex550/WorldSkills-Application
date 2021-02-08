package com.example.worldskills.models

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime
import java.util.*

data class Bankomat(
    val street: String,
    val isWork: Boolean,
    val name: String,
    val workStart: Calendar,
    val workEnd: Calendar,
    val geo: LatLng,
)