package com.example.loo_zfeng_33533008.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loo_zfeng_33533008.data.entity.User
import com.example.loo_zfeng_33533008.data.repository.UserRepository
import com.example.loo_zfeng_33533008.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _registrationSuccess = MutableStateFlow<Boolean?>(null)
    val registrationSuccess: StateFlow<Boolean?> = _registrationSuccess

    private val _navigationState = MutableStateFlow<Screen>(Screen.Welcome)
    val navigationState: StateFlow<Screen> = _navigationState

    fun navigateTo(screen: Screen) {
        _navigationState.value = screen
    }

    fun checkUserRegistration(userId: String) {
        viewModelScope.launch {
            try {
                val isRegistered = userRepository.isUserRegistered(userId)
                if (!isRegistered) {
                    _authState.value = AuthState.Error("User not registered. Please register first.")
                    navigateTo(Screen.Login)
                } else {
                    navigateTo(Screen.Login)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error checking user registration: ${e.message}")
            }
        }
    }

    fun registerUser(userId: String, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                userRepository.registerUser(userId, phoneNumber, password)
                _registrationSuccess.value = true
                _authState.value = AuthState.Initial
                navigateTo(Screen.Login)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Registration failed: ${e.message}")
                _registrationSuccess.value = false
            }
        }
    }

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.validateUser(userId, password)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                    navigateTo(Screen.Home)
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Initial
        _registrationSuccess.value = null
        navigateTo(Screen.Login)
    }
} 