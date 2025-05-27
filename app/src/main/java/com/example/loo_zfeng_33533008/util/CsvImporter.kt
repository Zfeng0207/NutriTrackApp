package com.example.loo_zfeng_33533008.util

import android.content.Context
import android.util.Log
import com.example.loo_zfeng_33533008.data.entity.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class CsvImporter(private val context: Context) {
    private val TAG = "CsvImporter"

    suspend fun importPatientsFromCsv(inputStream: java.io.InputStream): List<Patient> = withContext(Dispatchers.IO) {
        val patients = mutableListOf<Patient>()
        try {
            Log.d(TAG, "Starting CSV import process")
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                // Read and log header row
                val header = reader.readLine()
                Log.d(TAG, "CSV Header: $header")
                
                // Read data rows
                var lineNumber = 1
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    lineNumber++
                    line?.let { 
                        Log.d(TAG, "Processing line $lineNumber: $it")
                        processLine(it)?.let { patient ->
                            Log.d(TAG, "Successfully processed patient: userId=${patient.userId}, phone=${patient.phoneNumber}")
                            patients.add(patient)
                        } ?: Log.e(TAG, "Failed to process line $lineNumber")
                    }
                }
                Log.d(TAG, "Finished reading CSV file. Total lines processed: $lineNumber")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading CSV file", e)
            throw e
        }
        Log.d(TAG, "Total patients imported: ${patients.size}")
        patients
    }

    private fun processLine(line: String): Patient? {
        return try {
            val values = line.split(",")
            Log.d(TAG, "Split values count: ${values.size}")
            if (values.size >= 63) { // Updated to match actual CSV structure
                Patient(
                    userId = values[1].trim(), // User_ID is second column
                    phoneNumber = values[0].trim(), // PhoneNumber is first column
                    sex = values[2].trim(), // Sex is third column
                    heifaTotalScoreMale = values[3].toDoubleOrNull() ?: 0.0,
                    heifaTotalScoreFemale = values[4].toDoubleOrNull() ?: 0.0,
                    discretionaryHeifaScoreMale = values[5].toDoubleOrNull() ?: 0.0,
                    discretionaryHeifaScoreFemale = values[6].toDoubleOrNull() ?: 0.0,
                    discretionaryServeSize = values[7].toDoubleOrNull() ?: 0.0,
                    vegetablesHeifaScoreMale = values[8].toDoubleOrNull() ?: 0.0,
                    vegetablesHeifaScoreFemale = values[9].toDoubleOrNull() ?: 0.0,
                    vegetablesWithLegumesAllocatedServeSize = values[10].toDoubleOrNull() ?: 0.0,
                    legumesAllocatedVegetables = values[11].toDoubleOrNull() ?: 0.0,
                    vegetablesVariationsScore = values[12].toDoubleOrNull() ?: 0.0,
                    vegetablesCruciferous = values[13].toDoubleOrNull() ?: 0.0,
                    vegetablesTuberAndBulb = values[14].toDoubleOrNull() ?: 0.0,
                    vegetablesOther = values[15].toDoubleOrNull() ?: 0.0,
                    legumes = values[16].toDoubleOrNull() ?: 0.0,
                    vegetablesGreen = values[17].toDoubleOrNull() ?: 0.0,
                    vegetablesRedAndOrange = values[18].toDoubleOrNull() ?: 0.0,
                    fruitHeifaScoreMale = values[19].toDoubleOrNull() ?: 0.0,
                    fruitHeifaScoreFemale = values[20].toDoubleOrNull() ?: 0.0,
                    fruitServeSize = values[21].toDoubleOrNull() ?: 0.0,
                    fruitVariationsScore = values[22].toDoubleOrNull() ?: 0.0,
                    fruitPome = values[23].toDoubleOrNull() ?: 0.0,
                    fruitTropicalAndSubtropical = values[24].toDoubleOrNull() ?: 0.0,
                    fruitBerry = values[25].toDoubleOrNull() ?: 0.0,
                    fruitStone = values[26].toDoubleOrNull() ?: 0.0,
                    fruitCitrus = values[27].toDoubleOrNull() ?: 0.0,
                    fruitOther = values[28].toDoubleOrNull() ?: 0.0,
                    grainsAndCerealsHeifaScoreMale = values[29].toDoubleOrNull() ?: 0.0,
                    grainsAndCerealsHeifaScoreFemale = values[30].toDoubleOrNull() ?: 0.0,
                    grainsAndCerealsServeSize = values[31].toDoubleOrNull() ?: 0.0,
                    grainsAndCerealsNonWholeGrains = values[32].toDoubleOrNull() ?: 0.0,
                    wholeGrainsHeifaScoreMale = values[33].toDoubleOrNull() ?: 0.0,
                    wholeGrainsHeifaScoreFemale = values[34].toDoubleOrNull() ?: 0.0,
                    wholeGrainsServeSize = values[35].toDoubleOrNull() ?: 0.0,
                    meatAndAlternativesHeifaScoreMale = values[36].toDoubleOrNull() ?: 0.0,
                    meatAndAlternativesHeifaScoreFemale = values[37].toDoubleOrNull() ?: 0.0,
                    meatAndAlternativesWithLegumesAllocatedServeSize = values[38].toDoubleOrNull() ?: 0.0,
                    legumesAllocatedMeatAndAlternatives = values[39].toDoubleOrNull() ?: 0.0,
                    dairyAndAlternativesHeifaScoreMale = values[40].toDoubleOrNull() ?: 0.0,
                    dairyAndAlternativesHeifaScoreFemale = values[41].toDoubleOrNull() ?: 0.0,
                    dairyAndAlternativesServeSize = values[42].toDoubleOrNull() ?: 0.0,
                    sodiumHeifaScoreMale = values[43].toDoubleOrNull() ?: 0.0,
                    sodiumHeifaScoreFemale = values[44].toDoubleOrNull() ?: 0.0,
                    sodiumMgMilligrams = values[45].toDoubleOrNull() ?: 0.0,
                    alcoholHeifaScoreMale = values[46].toDoubleOrNull() ?: 0.0,
                    alcoholHeifaScoreFemale = values[47].toDoubleOrNull() ?: 0.0,
                    alcoholStandardDrinks = values[48].toDoubleOrNull() ?: 0.0,
                    waterHeifaScoreMale = values[49].toDoubleOrNull() ?: 0.0,
                    waterHeifaScoreFemale = values[50].toDoubleOrNull() ?: 0.0,
                    water = values[51].toDoubleOrNull() ?: 0.0,
                    waterTotalMl = values[52].toDoubleOrNull() ?: 0.0,
                    beverageTotalMl = values[53].toDoubleOrNull() ?: 0.0,
                    sugarHeifaScoreMale = values[54].toDoubleOrNull() ?: 0.0,
                    sugarHeifaScoreFemale = values[55].toDoubleOrNull() ?: 0.0,
                    sugar = values[56].toDoubleOrNull() ?: 0.0,
                    saturatedFatHeifaScoreMale = values[57].toDoubleOrNull() ?: 0.0,
                    saturatedFatHeifaScoreFemale = values[58].toDoubleOrNull() ?: 0.0,
                    saturatedFat = values[59].toDoubleOrNull() ?: 0.0,
                    unsaturatedFatHeifaScoreMale = values[60].toDoubleOrNull() ?: 0.0,
                    unsaturatedFatHeifaScoreFemale = values[61].toDoubleOrNull() ?: 0.0,
                    unsaturatedFatServeSize = values[62].toDoubleOrNull() ?: 0.0
                )
            } else {
                Log.e(TAG, "Invalid line format: expected 63+ columns, got ${values.size}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing line: $line", e)
            null
        }
    }
} 