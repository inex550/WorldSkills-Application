package com.example.worldskills.network

import com.example.worldskills.models.*
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object BankApi {

    private const val BASE_URL = "http://192.168.1.107:8080"

    private const val BANKOMATS_METHOD = "/bankomats"
    private const val VALUTE_METHOD = "/valute"
    private const val LOGIN_METHOD = "/login"
    private const val LOGOUT_METHOD = "/logout"

    private const val GETUSER_METHOD = "/getuser"
    private const val GETCARDS_METHOD = "/getcards"
    private const val GETCHECKS_METHOD = "/getcheck"
    private const val GETCREDITS_METHOD = "/getcredits"
    private const val LASTLOGIN_METHOD = "/lastlogin"

    private const val EDITLOGIN_METHOD = "/editelogin"
    private const val EDITPASSWORD_METHOD = "/editepassword"
    private const val BLOCK_METHOD = "/block"

    private const val CARD_HISTORY_METHOD = "/history/card"
    private const val CHECK_HISTORY_METHOD = "/history/check"

    private const val RENAME_CARD_METHOD = "/card/changename"
    private const val RENAME_CHECK_METHOD = "/check/changename"

    private const val REFILL_METHOD = "/refill"
    private const val PAY_METHOD = "/pay"
    private const val CATEGORY_METHOD = "/category"


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
        val (_, response) = NetworkService.get(BASE_URL + VALUTE_METHOD)

        val valutes = mutableMapOf<String, BankValute>()

        val valutesJson = JSONArray(response)
        for (i in 0 until valutesJson.length()) {
            val valuteJson = valutesJson.getJSONObject(i)
            val charCode = valuteJson.getString("charCode")
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

        val (code, response) = NetworkService.doJson(BASE_URL + LOGIN_METHOD, "POST", requestJson)

        if (code != 200)
            return null

        return JSONObject(response).getString("token")
    }

    fun logout(token: String): Boolean {
        val tokenJson = JSONObject()
                .put("token", token)

        val (code, _) = NetworkService.doJson(BASE_URL + LOGOUT_METHOD, "DELETE", tokenJson)

        return code == 200
    }

    fun getUser(token: String): UserInfo? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.doJson(BASE_URL + GETUSER_METHOD, "POST", tokenJson)

        if (code != 200) return null

        val userInfoJson = JSONObject(response)

        return UserInfo(
                firstName = userInfoJson.getString("first_name"),
                lastName = userInfoJson.getString("last_name"),
                patronymic = userInfoJson.getString("patronymic")
        )
    }

    fun getCards(token: String): List<Card>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.doJson(BASE_URL + GETCARDS_METHOD, "POST", tokenJson)

        if (code != 200) return null

        val cards = mutableListOf<Card>()

        val cardsJson = JSONArray(response)
        for (i in 0 until cardsJson.length()) {
            val cardJson = cardsJson.getJSONObject(i)
            val card = Card(
                    name = cardJson.getString("name"),
                    num = cardJson.getString("num"),
                    type = cardJson.getString("card_type"),
                    cash = cardJson.getInt("cash"),
                    blocked = cardJson.getBoolean("blocked")
            )
            cards.add(card)
        }

        return cards
    }

    fun getChecks(token: String): List<Check>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.doJson(BASE_URL + GETCHECKS_METHOD, "POST", tokenJson)

        if (code != 200) return null

        val checks = mutableListOf<Check>()

        val checksJson = JSONArray(response)
        for (i in 0 until checksJson.length()) {
            val checkJson = checksJson.getJSONObject(i)
            val check = Check(
                    num = checkJson.getString("num"),
                    name = checkJson.getString("name"),
                    cash = checkJson.getInt("cash")
            )
            checks.add(check)
        }

        return checks
    }

    fun getCredits(token: String): List<Credit>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.doJson(BASE_URL + GETCREDITS_METHOD, "POST", tokenJson)

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

    fun editLogin(token: String, login: String): Boolean {
        val requestJson = JSONObject()
                .put("token", token)
                .put("login", login)
        val (code, _) = NetworkService.doJson(BASE_URL + EDITLOGIN_METHOD, "PUT", requestJson)

        return code == 200
    }

    fun editPassword(token: String, password: String): Boolean {
        val requestJson = JSONObject()
                .put("token", token)
                .put("password", password)

        val (code, _) = NetworkService.doJson(BASE_URL + EDITPASSWORD_METHOD, "PUT", requestJson)

        return code == 200
    }

    fun lastLogin(token: String): List<LastLogin>? {
        val tokenJson = JSONObject().put("token", token)
        val (code, response) = NetworkService.doJson(BASE_URL + LASTLOGIN_METHOD, "POST", tokenJson)

        if (code != 200) return null

        val lastLoginsJson = JSONArray(response)

        val lastLogins = mutableListOf<LastLogin>()

        for (i in 0 until lastLoginsJson.length()) {
            val lastLoginJson = lastLoginsJson.getJSONObject(i)

            val date = Calendar.getInstance()
            date.time = sdfDate.parse(lastLoginJson.getString("login_date"))!!

            val time = Calendar.getInstance()
            time.time = sdfTime.parse(lastLoginJson.getString("login_time"))!!

            val lastLogin = LastLogin(
                    date = date,
                    time = time
            )

            lastLogins.add(lastLogin)
        }

        return lastLogins
    }

    fun getCardOperations(token: String, cardNum: String): List<Operation>? {
        val requestJson = JSONObject()
                .put("token", token)
                .put("card_number", cardNum)

        val (code, response) = NetworkService.doJson(BASE_URL + CARD_HISTORY_METHOD, "POST", requestJson)

        if (code != 200) return null

        val cardOpsJson = JSONArray(response)
        val cardOps = mutableListOf<Operation>()

        for (i in 0 until cardOpsJson.length()) {
            val cardOpJson = cardOpsJson.getJSONObject(i)
            val cardOp = Operation(
                    name = cardOpJson.getString("name"),
                    date = sdfDate.parse(cardOpJson.getString("date"))!!,
                    cash = cardOpJson.getInt("cash")
            )
            cardOps.add(cardOp)
        }

        return cardOps
    }

    fun getCheckOperation(token: String, checkNum: String): List<Operation>? {
        val requestJson = JSONObject()
                .put("token", token)
                .put("check_number", checkNum)

        val (code, response) = NetworkService.doJson(BASE_URL + CHECK_HISTORY_METHOD, "POST", requestJson)

        if (code != 200) return null

        val checkOpsJson = JSONArray(response)
        val checkOps = mutableListOf<Operation>()

        for (i in 0 until checkOpsJson.length()) {
            val checkOpJson = checkOpsJson.getJSONObject(i)
            val checkOp = Operation(
                    name = checkOpJson.getString("name"),
                    date = sdfDate.parse(checkOpJson.getString("date"))!!,
                    cash = checkOpJson.getInt("cash")
            )
            checkOps.add(checkOp)
        }

        return checkOps
    }

    fun block(token: String, cardNum: String): Boolean {
        val requestJson = JSONObject()
                .put("token", token)
                .put("card_number", cardNum)

        val (code, response) = NetworkService.doJson(BASE_URL + BLOCK_METHOD, "POST", requestJson)

        return code == 200 && JSONObject(response).getBoolean("ok")
    }

    fun changeCardName(token: String, cardNum: String, newName: String): Boolean {
        val requestJson = JSONObject()
                .put("token", token)
                .put("card_number", cardNum)
                .put("new_name", newName)

        val (code, _) = NetworkService.doJson(BASE_URL + RENAME_CARD_METHOD, "POST", requestJson)

        return code == 200
    }

    fun changeCheckName(token: String, checkNum: String, newName: String): Boolean {
        val requestJson = JSONObject()
                .put("token", token)
                .put("check_number", checkNum)
                .put("new_name", newName)

        val (code, _) = NetworkService.doJson(BASE_URL + RENAME_CHECK_METHOD, "POST", requestJson)

        return code == 200
    }

    fun refill(token: String, sourceCardNum: String, destCardNum: String, sum: Int): Int {
        val requestJson = JSONObject()
                .put("token", token)
                .put("card_number_sourse", sourceCardNum)
                .put("card_number_dest", destCardNum)
                .put("sum", sum)

        val (code, _) = NetworkService.doJson(BASE_URL + REFILL_METHOD, "POST", requestJson)

        return code
    }

    fun pay(token: String, sourceCardNum: String, destCheckNum: String, sum: Int): Int {
        val requestJson = JSONObject()
                .put("token", token)
                .put("card_number_sourse", sourceCardNum)
                .put("number_check", destCheckNum)
                .put("sum", sum)

        val (code, _) = NetworkService.doJson(BASE_URL + PAY_METHOD, "POST", requestJson)

        return code
    }

    fun loadCategory(token: String): List<Payment>? {
        val requestJson = JSONObject()
                .put("token", token)

        val (code, response) = NetworkService.doJson(BASE_URL + CATEGORY_METHOD, "POST", requestJson)

        if (code != 200) return null

        val paymentsJson = JSONArray(response)
        val payments = mutableListOf<Payment>()

        for (i in 0 until paymentsJson.length()) {
            val paymentJson = paymentsJson.getJSONObject(i)

            val payment = Payment(
                    name = paymentJson.getString("name")
            )

            payments.add(payment)
        }

        return payments
    }
}