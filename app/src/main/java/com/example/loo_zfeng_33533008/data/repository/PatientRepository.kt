package com.example.loo_zfeng_33533008.data.repository

import com.example.loo_zfeng_33533008.data.dao.PatientDao
import com.example.loo_zfeng_33533008.data.entity.Patient
import kotlinx.coroutines.flow.Flow

class PatientRepository(
    private val patientDao: PatientDao
) {
    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun getPatientById(userId: String): Patient? = patientDao.getPatientById(userId)

    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String): Patient? =
        patientDao.getPatientByIdAndPhone(userId, phoneNumber)

    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient? =
        patientDao.getPatientByIdAndPassword(userId, password)

    suspend fun insertPatient(patient: Patient) = patientDao.insertPatient(patient)

    suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)

    suspend fun deleteAllPatients() = patientDao.deleteAllPatients()
} 