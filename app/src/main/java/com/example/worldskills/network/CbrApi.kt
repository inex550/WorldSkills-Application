package com.example.worldskills.network

import com.example.worldskills.models.Valute
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object CbrApi {
    private const val BASE_URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req="

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    fun loadValutes(date: Calendar): List<Valute> {
        val strDate = sdf.format(date.time)

        val response = NetworkService.get(BASE_URL + strDate, "Windows-1251")

        return ValuteParser.parse(response)
    }
}