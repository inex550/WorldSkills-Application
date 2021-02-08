package com.example.worldskills.network

import com.example.worldskills.models.Bankomat
import org.json.JSONArray
import java.net.URL
import java.time.LocalTime

object BankApi {

    const val BASE_URL = "http://192.168.1.107:8080"

    const val BANKOMATS_URL = "/bankomats"

    fun loadBankomats(): List<Bankomat> {
        val response = URL(BASE_URL + BANKOMATS_URL).readText()

        val bnks = mutableListOf<Bankomat>()

        val bnksJson = JSONArray(response)
        for (i in 0 until bnksJson.length()) {
            val bnkJson = bnksJson.getJSONObject(i)
            bnks.add(Bankomat(
                street = bnkJson.getString("street"),
                isWork = bnkJson.getBoolean("is_work"),
                name = bnkJson.getString("name"),
                workStart = bnkJson.getString("work_start"),
                workEnd = bnkJson.getString("work_end"),
                geoLat = bnkJson.getDouble("geo_lat"),
                geoLng = bnkJson.getDouble("geo_lng")
            ))
        }

        return bnks
    }
}