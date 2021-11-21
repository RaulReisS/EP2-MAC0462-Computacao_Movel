package br.com.raulreis.recipe.Common

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Common {
    val API_KEY = "e3226d5a710ed7b606cbe4443abb8c31"
    val API_LINK = "https://api.openweathermap.org/data/2.5/weather?"
    val unitsSystem = "metric"


    //${BASE_WEATHER_URL}lat=${latitude}&lon=${longitude}&units=${unitsSystem}&lang=pt_br&appid=${WEATHER_API_KEY}

    fun apiRequest(lat:String, long:String) : String {
        var sb = StringBuilder(API_LINK)
        sb.append("lat=${lat}&lon=${long}&units=${unitsSystem}&lang=pt_br&appid=${API_KEY}")
        return sb.toString()
    }

    fun unixTimeStampToDateTime(unixTimeStamp : Double) : String {
        val dateFormat = SimpleDateFormat("HH:mm")
        val date = Date()
        date.time = unixTimeStamp.toLong()*1000
        return dateFormat.format(date)

    }

    fun getImage (icon : String) : String {
        return "https://openweathermap.org/img/wn/${icon}@4x.png"
    }

    val dateNow : String
        get() {
            val dateFormat = SimpleDateFormat("dd MM yyyy HH:mm")
            val date=Date()
            return dateFormat.format(date)
        }
}