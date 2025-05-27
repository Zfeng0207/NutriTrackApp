package com.example.loo_zfeng_33533008.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loo_zfeng_33533008.data.repository.PatientRepository
import com.example.loo_zfeng_33533008.data.repository.UserRepository
import com.example.loo_zfeng_33533008.ui.viewmodel.ClinicianDashboardViewModel

class ClinicianDashboardViewModelFactory(
    private val patientRepository: PatientRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicianDashboardViewModel::class.java)) {
            return ClinicianDashboardViewModel(patientRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 