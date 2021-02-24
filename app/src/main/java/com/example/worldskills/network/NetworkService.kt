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

    fun doJson(url: String, method: String, json: JSONObject, encoding: String = "utf-8"): Pair<Int, String> {
        with (URL(url).openConnection() as HttpURLConnection) {
            requestMethod = method
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")

            doOutput = true

            val osw = OutputStreamWriter(outputStream, encoding)
            osw.write(json.toString())
            osw.close()

            val connInputStream = if (responseCode >= 400)
                errorStream
            else inputStream

            val isr = InputStreamReader(connInputStream, encoding)
            val response = isr.readText()

            return Pair(responseCode, response)
        }
    }
}