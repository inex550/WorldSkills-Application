package com.example.worldskills.network

import com.example.worldskills.models.Valute
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object CbrApi {
    private const val BASE_URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req="

    fun loadValutes(date: Calendar): List<Valute> {
        val strDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date.time)

        val response = URL(BASE_URL + strDate).readText()

        return ValuteParser.parse(response)
    }
}