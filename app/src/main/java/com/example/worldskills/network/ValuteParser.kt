package com.example.worldskills.network

import android.util.Xml
import com.example.worldskills.models.Valute
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

object ValuteParser {

    fun parse(xmlStr: String): List<Valute> {
        val parser = Xml.newPullParser()
        parser.setInput(StringReader(xmlStr))

        parser.nextTag()

        return readValCurs(parser)
    }

    fun readValCurs(parser: XmlPullParser): List<Valute> {
        val valutes = mutableListOf<Valute>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue

            if (parser.name == "Valute")
                valutes.add( parseValute(parser) )
        }

        return valutes
    }

    fun parseValute(parser: XmlPullParser): Valute {
        var numCode: String? = null
        var charCode: String? = null
        var nominal: Int? = null
        var name: String? = null
        var value: Double? = null

        while (parser.eventType != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "NumCode" -> numCode = readText(parser)
                "CharCode" -> charCode = readText(parser)
                "Nominal" -> nominal = readText(parser).toInt()
                "Name" -> name = readText(parser)
                "Value" -> value = readText(parser).replace(',', '.').toDouble()
                "Valute" -> parser.nextTag()
            }
        }

        return Valute(numCode, charCode, nominal, name, value)
    }

    fun readText(parser: XmlPullParser): String {
        val result = parser.nextText()
        parser.nextTag()

        return result
    }
}