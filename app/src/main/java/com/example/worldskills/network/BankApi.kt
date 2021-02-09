package com.example.worldskills.network

import com.example.worldskills.models.BankValute
import com.example.worldskills.models.Bankomat
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object BankApi {

    private const val BASE_URL = "http://192.168.0.128:8080"

    private const val BANKOMATS_METHOD = "/bankomats"
    private const val VALUTE_METHID = "/valute"

    fun loadBankomats(): List<Bankomat> {
        val response = URL(BASE_URL + BANKOMATS_METHOD).readText()

        val bnks = mutableListOf<Bankomat>()

        val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

        val bnksJson = JSONArray(response)
        for (i in 0 until bnksJson.length()) {
            val bnkJson = bnksJson.getJSONObject(i)

            val workStart = Calendar.getInstance()
            val workEnd = Calendar.getInstance()

            workStart.time = sdf.parse(bnkJson.getString("work_start"))!!
            workEnd.time = sdf.parse(bnkJson.getString("work_end"))!!

            bnks.add(Bankomat(
                street = bnkJson.getString("street"),
                isWork = bnkJson.getBoolean("is_work"),
                name = bnkJson.getString("name"),
                workStart = workStart,
                workEnd = workEnd,
                geo = LatLng(bnkJson.getDouble("geo_lat"), bnkJson.getDouble("geo_lng"))
            ))
        }

        return bnks
    }

    fun loadValutes(): Map<String, BankValute> {
        val response = URL(BASE_URL + BANKOMATS_METHOD).readText()

        val valutes = mutableMapOf<String, BankValute>()

        val valutesJson = JSONArray(response)
        for (i in 0 until valutesJson.length()) {
            val valuteJson = valutesJson.getJSONObject(i)
            val charCode =valuteJson.getString("charCode")
            val valute = BankValute(
                buy = valuteJson.getDouble("buy"),
                cell = valuteJson.getDouble("cell"),
                charCode = charCode
            )

            valutes[charCode] = valute
        }

        return valutes
    }
}