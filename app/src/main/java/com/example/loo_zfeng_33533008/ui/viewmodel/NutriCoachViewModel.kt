package com.example.loo_zfeng_33533008.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loo_zfeng_33533008.data.entity.MotivationalMessage
import com.example.loo_zfeng_33533008.data.repository.MotivationalMessageRepository
import com.example.loo_zfeng_33533008.util.GeminiApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// Data class for fruit details - matches Fruityvice API response structure
data class FruitDetails(
    val family: String = "",
    val calories: Double = 0.0,
    val fat: Double = 0.0,
    val sugar: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val protein: Double = 0.0
    // API also returns 'genus', 'name', 'id', 'order', but we only use these for the table
)

class NutriCoachViewModel(
    private val messageRepo: MotivationalMessageRepository,
    private val authViewModel: AuthViewModel // Inject AuthViewModel
) : ViewModel() {

    private val _fruitDetails = MutableStateFlow<FruitDetails?>(null)
    val fruitDetails: StateFlow<FruitDetails?> = _fruitDetails

    private val _messages = MutableStateFlow<List<MotivationalMessage>>(emptyList())
    val messages: StateFlow<List<MotivationalMessage>> = _messages

    // User ID collected from AuthViewModel
    private var currentUserId: String? = null

    init {
        // Collect user ID when the ViewModel is created
        viewModelScope.launch {
            authViewModel.currentUser.collectLatest { user ->
                currentUserId = user?.userId
                // Optionally fetch messages when user logs in or changes
                if (currentUserId != null) {
                    fetchMotivationalMessages(currentUserId!!)
                }
            }
        }
    }

    // Function to fetch fruit details from Fruityvice API
    fun fetchFruitDetails(fruitName: String) {
        viewModelScope.launch {
            try {
                val details = getFruitDetailsFromApi(fruitName)
                _fruitDetails.value = details
            } catch (e: Exception) {
                _fruitDetails.value = null // Clear previous data on error
                // Log the error or show a user-friendly message
                e.printStackTrace()
            }
        }
    }

    // Helper function to make the actual API call (runs on IO dispatcher)
    private suspend fun getFruitDetailsFromApi(fruitName: String): FruitDetails? = withContext(Dispatchers.IO) {
        val urlString = "https://www.fruityvice.com/api/fruit/$fruitName"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.use { it.readText() }
                val json = JSONObject(response)
                val nutritions = json.getJSONObject("nutritions")

                FruitDetails(
                    family = json.optString("family", "N/A"),
                    calories = nutritions.optDouble("calories", 0.0),
                    fat = nutritions.optDouble("fat", 0.0),
                    sugar = nutritions.optDouble("sugar", 0.0),
                    carbohydrates = nutritions.optDouble("carbohydrates", 0.0),
                    protein = nutritions.optDouble("protein", 0.0)
                )
            } else {
                // Handle non-HTTP_OK responses (e.g., fruit not found)
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null on error
        } finally {
            connection.disconnect()
        }
    }

    // fetchMotivationalMessages now requires userId as parameter
    fun fetchMotivationalMessages(userId: String) {
        viewModelScope.launch {
            messageRepo.getMessagesForUser(userId).collect { messages ->
                _messages.value = messages
            }
        }
    }

    fun generateAndSaveMotivationalMessage(apiKey: String) {
        // Use the collected userId if available
        val userId = currentUserId
        if (userId != null) {
            viewModelScope.launch {
                val prompt = "Write a friendly motivational message encouraging someone to eat more fruits today."
                val aiMessage = GeminiApiHelper.generateMessage(prompt, apiKey)
                saveMotivationalMessage(message = aiMessage, userId = userId) // Pass userId here
            }
        } else {
            // Handle case where user ID is not available (e.g., not logged in)
            // Perhaps update a state flow to show an error message
        }
    }

    // saveMotivationalMessage now requires userId as parameter
    fun saveMotivationalMessage(message: String, userId: String) {
        viewModelScope.launch {
            messageRepo.insertMessage(MotivationalMessage(message = message, userId = userId)) // Include userId when saving
            fetchMotivationalMessages(userId) // Use userId here
        }
    }
}