package com.example.worldskills.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object NetworkService {

    fun get(url: String, encoding: String="utf-8"): Pair<Int, String> {
        with (URL(url).openConnection() as HttpURLConnection) {
            val isr = InputStreamReader(inputStream, encoding)
            return Pair(responseCode, isr.readText())
        }
    }

    fun postJson(url: String, json: JSONObject, encoding: String="utf-8"): Pair<Int, String> {
        with (URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json; utf-8")

            doOutput = true

            val requestData = json.toString()

            val osw = OutputStreamWriter(outputStream, encoding)
            osw.write(requestData)
            osw.close()

            val connInputStream = if (responseCode >= 400) errorStream else inputStream

            val isr = InputStreamReader(connInputStream, encoding)
            return Pair(responseCode, isr.readText())
        }
    }

    fun deleteJson(url: String, json: JSONObject, encoding: String="utf-8"): Pair<Int, String> {
        with (URL(url).openConnection() as HttpURLConnection) {
            requestMethod = "DELETE"
            setRequestProperty("Content-Type", "application/json")

            doOutput = true

            val osw = OutputStreamWriter(outputStream, encoding)
            osw.write(json.toString())
            osw.close()

            val connInputStream = if (responseCode >= 400)
                errorStream
            else inputStream

            val isr = InputStreamReader(connInputStream)
            val response = isr.readText()

            return Pair(responseCode, response)
        }
    }
}