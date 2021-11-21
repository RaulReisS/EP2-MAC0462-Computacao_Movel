package br.com.raulreis.recipe.Common

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Helper {
    fun getGTTPData(urlStrign: String?): String? {
        try {
            val url = URL(urlStrign)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            if (urlConnection.getResponseCode() === HttpURLConnection.HTTP_OK) {
                val r = BufferedReader(InputStreamReader(urlConnection.getInputStream()))
                val sb = StringBuilder()
                var line: String?
                while (r.readLine().also { line = it } != null) sb.append(line)
                stream = sb.toString()
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stream
    }

    companion object {
        var stream: String? = null
    }
}