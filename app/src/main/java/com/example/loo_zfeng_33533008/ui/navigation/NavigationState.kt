package com.example.loo_zfeng_33533008.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Home : Screen("home")
    object Insights : Screen("insights")
    object NutriCoach : Screen("nutricoach")
    object ClinicianLogin : Screen("clinician_login") // <-- Make sure this line exists
    object ClinicianDashboard : Screen("clinician_dashboard") // <-- Make sure this line exists
    // You might also want an object for Settings if you use its route often
    object Settings : Screen("settings")
}

sealed class NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}