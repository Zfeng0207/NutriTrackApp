package com.example.loo_zfeng_33533008.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loo_zfeng_33533008.data.repository.MotivationalMessageRepository
import com.example.loo_zfeng_33533008.ui.viewmodel.AuthViewModel
import com.example.loo_zfeng_33533008.ui.viewmodel.NutriCoachViewModel

class NutriCoachViewModelFactory(
    private val messageRepo: MotivationalMessageRepository,
    private val authViewModel: AuthViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            return NutriCoachViewModel(messageRepo, authViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 