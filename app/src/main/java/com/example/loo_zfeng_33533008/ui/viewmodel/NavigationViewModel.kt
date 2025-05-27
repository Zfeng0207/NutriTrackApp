package com.example.loo_zfeng_33533008.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.loo_zfeng_33533008.ui.navigation.NavigationEvent
import com.example.loo_zfeng_33533008.ui.navigation.Screen

class NavigationViewModel : ViewModel() {
    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun navigateTo(route: String) {
        _navigationEvent.value = NavigationEvent.NavigateTo(route)
    }

    fun navigateBack() {
        _navigationEvent.value = NavigationEvent.NavigateBack
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
} 
