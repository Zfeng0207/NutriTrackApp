package com.example.loo_zfeng_33533008.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.loo_zfeng_33533008.data.repository.PatientRepository
import com.example.loo_zfeng_33533008.util.CsvImporter
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "settings")

class DatabaseInitializer(
    private val context: Context,
    private val patientRepository: PatientRepository
) {
    private val csvImporter = CsvImporter(context)
    private val TAG = "DatabaseInitializer"

    suspend fun initializeIfNeeded() {
        try {
            val isInitialized = context.dataStore.data.first()[IS_INITIALIZED] ?: false
            Log.d(TAG, "Checking initialization status: isInitialized=$isInitialized")
            
            if (!isInitialized) {
                Log.d(TAG, "Starting database initialization")
                try {
                    context.assets.open("user_data.csv").use { inputStream ->
                        val patients = csvImporter.importPatientsFromCsv(inputStream)
                        Log.d(TAG, "Imported ${patients.size} patients from CSV")

                        if (patients.isNotEmpty()) {
                            patients.forEach { patient ->
                                try {
                                    patientRepository.insertPatient(patient)
                                    Log.d(TAG, "Inserted patient: ${patient.userId}")
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error inserting patient ${patient.userId}", e)
                                }
                            }
                            
                            // Only mark as initialized if we successfully imported patients
                            context.dataStore.edit { preferences ->
                                preferences[IS_INITIALIZED] = true
                            }
                            Log.d(TAG, "Database initialization completed successfully")
                        } else {
                            Log.e(TAG, "No patients were imported from CSV")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error during CSV import", e)
                    throw e
                }
            } else {
                Log.d(TAG, "Database already initialized")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during database initialization", e)
            throw e
        }
    }

    companion object {
        val IS_INITIALIZED = booleanPreferencesKey("is_initialized")

        suspend fun resetInitFlag(context: Context) {
            context.dataStore.edit {
                it[IS_INITIALIZED] = false
            }
            Log.d("DatabaseInitializer", "Initialization flag reset.")
        }
    }
}

 