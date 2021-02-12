package com.example.worldskills.network

import com.example.worldskills.models.*
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object BankApi {

    private const val BASE_URL = "http://192.168.0.34:8080"

    private const val BANKOMATS_METHOD = "/bankomats"
    private const val VALUTE_METHOD = "/valute"
    private const val LOGIN_METHOD = "/login"
    private const val LOGOUT_METHOD = "/logout"

    private const val GETCARDS_METHOD = "/getcards"
    private const val GETCHECKS_METHOD = "/getcheck"
    private const val GETCREDITS_METHOD = "/getcredits"


    val sdfTime = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)


    fun loadBankomats(): List<Bankomat> {
        val (_, response) = NetworkService.get(BASE_URL + BANKOMATS_METHOD)

        val bnks = mutableListOf<Bankomat>()

        val bnksJson = JSONArray(response)
        for (i in 0 until bnksJson.length()) {
            val bnkJson = bnksJson.getJSONObject(i)

            val workStart = Calendar.getInstance()
            val workEnd = Calendar.getInstance()

            workStart.time = sdfTime.parse(bnkJson.getString("work_start"))!!
            workEnd.time = sdfTime.parse(bnkJson.getString("work_end"))!!

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

    fun login(login: String, password: String): String? {
        val requestJson = JSONObject()
                .put("login", login)
                .put("password", password)

        val (code, response) = NetworkService.postJson(BASE_URL + LOGIN_METHOD, requestJson)

        if (code != 200)
            return null

        return JSONObject(response).getString("token")
    }

    fun logout(token: String): Boolean {
        val tokenJson = JSONObject()
            .put("token", token)

        val (code, _) = NetworkService.deleteJson(BASE_URL + LOGOUT_METHOD, tokenJson)

        return code == 200
    }

    fun getCards(token: String): List<Card>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.postJson(BASE_URL + GETCARDS_METHOD, tokenJson)

        if (code != 200) return null

        val cards = mutableListOf<Card>()

        val cardsJson = JSONArray(response)
        for (i in 0 until cardsJson.length()) {
            val cardJson = cardsJson.getJSONObject(i)
            val card = Card(
                    name = cardJson.getString("name"),
                    num = cardJson.getString("num"),
                    type = cardJson.getString("card_type"),
                    cash = cardJson.getInt("cash")
            )
            cards.add(card)
        }

        return cards
    }

    fun getChecks(token: String): List<Check>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.postJson(BASE_URL + GETCHECKS_METHOD, tokenJson)

        if (code != 200) return null

        val checks = mutableListOf<Check>()

        val checksJson = JSONArray(response)
        for (i in 0 until checksJson.length()) {
            val checkJson = checksJson.getJSONObject(i)
            val check = Check(
                    num = checkJson.getString("num"),
                    cash = checkJson.getInt("cash")
            )
            checks.add(check)
        }

        return checks
    }

    fun getCredits(token: String): List<Credit>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.postJson(BASE_URL + GETCREDITS_METHOD, tokenJson)

        if (code != 200) return null

        val credits = mutableListOf<Credit>()

        val creditsJson = JSONArray(response)
        for (i in 0 until creditsJson.length()) {
            val creditJson = creditsJson.getJSONObject(i)

            val endDate = Calendar.getInstance()
            endDate.time = sdfDate.parse(creditJson.getString("end_date"))!!

            val credit = Credit(
                    name = creditJson.getString("name"),
                    cash = creditJson.getInt("cash"),
                    endDate = endDate
            )
            credits.add(credit)
        }

        return credits
    }
}