package com.example.loo_zfeng_33533008.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object GeminiApiHelper {
    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"

    suspend fun generateMessage(prompt: String, apiKey: String): String =
        withContext(Dispatchers.IO) {
            if (apiKey.isBlank() || apiKey == "YOUR_GEMINI_API_KEY") {
                throw IllegalArgumentException("Invalid API key. Please provide a valid Gemini API key.")
            }

            val url = URL("$BASE_URL?key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = JSONObject().apply {
                    put("contents", JSONArray().put(JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().apply {
                            put("text", prompt)
                        }))
                    }))
                }.toString()

                connection.outputStream.use { os ->
                    os.write(requestBody.toByteArray())
                    os.flush()
                }

                val responseCode = connection.responseCode
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    val errorMessage =
                        connection.errorStream?.bufferedReader()?.use { it.readText() }
                            ?: "Unknown error"
                    throw Exception("API call failed with code $responseCode: $errorMessage")
                }

                val response = connection.inputStream.bufferedReader().use { it.readText() }

                // ✅ Correct response parsing
                val jsonResponse = JSONObject(response)
                val candidatesArray = jsonResponse.getJSONArray("candidates")
                val content = candidatesArray.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")
                parts.getJSONObject(0).getString("text")
            } catch (e: Exception) {
                throw Exception("Failed to generate message: ${e.message}", e)
            } finally {
                connection.disconnect()
            }
        }
}
