package com.example.worldskills.network

import android.service.autofill.UserData
import com.example.worldskills.models.BankValute
import com.example.worldskills.models.Bankomat
import com.example.worldskills.models.UserSecret
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object BankApi {

    private const val BASE_URL = "http://192.168.1.107:8080"

    private const val BANKOMATS_METHOD = "/bankomats"
    private const val VALUTE_METHOD = "/valute"
    private const val LOGIN_METHOD = "/login"
    private const val LOGOUT_METHOD = "/logout"

    fun loadBankomats(): List<Bankomat> {
        val (_, response) = NetworkService.get(BASE_URL + BANKOMATS_METHOD)

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
        val (code, response) = NetworkService.get(BASE_URL + VALUTE_METHOD)

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

    fun login(login: String, password: String): UserSecret? {
        val requestJson = JSONObject()
                .put("login", login)
                .put("password", password)

        val (code, response) = NetworkService.postJson(BASE_URL + LOGIN_METHOD, requestJson)

        if (code != 200)
            return null

        val responseJson = JSONObject(response)

        val respLogin = responseJson.getString("login")
        val respToken = responseJson.getString("token")
        val respId = responseJson.getInt("id")

        return UserSecret(respLogin, respToken, respId)
    }

    fun logout(userSecret: UserSecret): Boolean {
        val requestJson = JSONObject()
            .put("id", userSecret.id)
            .put("token", userSecret.token)

        val (code, _) = NetworkService.deleteJson(BASE_URL + LOGOUT_METHOD, requestJson)

        return code == 200
    }
}