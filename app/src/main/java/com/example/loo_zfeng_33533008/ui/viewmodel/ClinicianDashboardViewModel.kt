package com.example.loo_zfeng_33533008.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loo_zfeng_33533008.data.repository.PatientRepository
import com.example.loo_zfeng_33533008.util.GeminiApiHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ClinicianDashboardViewModel(
    private val patientRepository: PatientRepository // Inject PatientRepository
) : ViewModel() {

    private val _patterns = MutableStateFlow<List<String>>(emptyList())
    val patterns: StateFlow<List<String>> = _patterns.asStateFlow()

    private val _showPatterns = MutableStateFlow(false)
    val showPatterns: StateFlow<Boolean> = _showPatterns.asStateFlow()

    private val _avgHeifaMale = MutableStateFlow(0.0)
    val avgHeifaMale: StateFlow<Double> = _avgHeifaMale.asStateFlow()

    private val _avgHeifaFemale = MutableStateFlow(0.0)
    val avgHeifaFemale: StateFlow<Double> = _avgHeifaFemale.asStateFlow()

    init {
        calculateAverages()
    }

    private fun calculateAverages() {
        viewModelScope.launch {
            val patients = patientRepository.getAllPatients().first() // Get all patients
            if (patients.isNotEmpty()) {
                val malePatients = patients.filter { it.sex.equals("Male", ignoreCase = true) }
                val femalePatients = patients.filter { it.sex.equals("Female", ignoreCase = true) }

                val maleAvg = if (malePatients.isNotEmpty()) {
                    malePatients.map { it.heifaTotalScoreMale }.average()
                } else 0.0

                val femaleAvg = if (femalePatients.isNotEmpty()) {
                    femalePatients.map { it.heifaTotalScoreFemale }.average()
                } else 0.0

                _avgHeifaMale.value = maleAvg
                _avgHeifaFemale.value = femaleAvg
            }
        }
    }

    // Find Data Patterns using AI
    fun findDataPatterns(apiKey: String) {
        viewModelScope.launch {
            _showPatterns.value = false
            _patterns.value = listOf("Analyzing data...")

            try {
                val patients = patientRepository.getAllPatients().first()

                if (patients.isEmpty()) {
                    _patterns.value = listOf("No patient data available.")
                    _showPatterns.value = true
                    return@launch
                }

                val dataForPrompt = patients.joinToString("\n") { patient ->
                    """
                PhoneNumber: ${patient.phoneNumber}, 
                Patient ID: ${patient.userId}, 
                Sex: ${patient.sex}, 
                HEIFA Total Score (Male): ${patient.heifaTotalScoreMale}, 
                HEIFA Total Score (Female): ${patient.heifaTotalScoreFemale}, 
                Fruit Score (Male): ${patient.fruitHeifaScoreMale}, 
                Fruit Score (Female): ${patient.fruitHeifaScoreFemale}, 
                Water Score (Male): ${patient.waterHeifaScoreMale}, 
                Water Score (Female): ${patient.waterHeifaScoreFemale}, 
                Saturated Fat Score (Female): ${patient.saturatedFatHeifaScoreFemale}, 
                Saturated Fat (serves): ${patient.saturatedFat}
                """.trimIndent()
                }

                val topics = listOf(
                    "fruit intake trends",
                    "hydration and water consumption patterns",
                    "saturated fat intake issues"
                )

                val insights = coroutineScope {
                    topics.map { topic ->
                        async {
                            val prompt = """
                            From the patient nutrition data below, generate ONE clear, data-driven insight about $topic. Focus only on patterns in HEIFA-related scores.

                            Return the result in 1 sentence of plain text.

                            Data:
                            $dataForPrompt
                        """.trimIndent()

                            GeminiApiHelper.generateMessage(prompt, apiKey)
                        }
                    }.awaitAll()
                }

                _patterns.value = insights
            } catch (e: Exception) {
                _patterns.value = listOf("Error: ${e.message}")
            } finally {
                _showPatterns.value = true
            }
        }
    }}
