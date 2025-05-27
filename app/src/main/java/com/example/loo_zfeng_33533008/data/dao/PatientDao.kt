package com.example.loo_zfeng_33533008.data.dao

import androidx.room.*
import com.example.loo_zfeng_33533008.data.entity.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientById(userId: String): Patient?

    @Query("SELECT * FROM patients WHERE userId = :userId AND phoneNumber = :phoneNumber")
    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String): Patient?

    @Query("SELECT * FROM patients WHERE userId = :userId AND password = :password")
    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Query("DELETE FROM patients")
    suspend fun deleteAllPatients()
} 