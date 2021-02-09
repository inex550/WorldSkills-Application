package com.example.worldskills.network

import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkService {

    fun get(url: String, encoding: String="utf-8"): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        val isr = InputStreamReader(conn.inputStream, encoding)
        val response = isr.readText()
        conn.disconnect()

        return response
    }
}