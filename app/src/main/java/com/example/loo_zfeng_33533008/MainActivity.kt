package com.example.loo_zfeng_33533008

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loo_zfeng_33533008.data.AppDatabase
import com.example.loo_zfeng_33533008.data.DatabaseInitializer
import com.example.loo_zfeng_33533008.data.repository.PatientRepository
import com.example.loo_zfeng_33533008.data.repository.UserRepository
import com.example.loo_zfeng_33533008.data.repository.MotivationalMessageRepository
import com.example.loo_zfeng_33533008.ui.navigation.Screen
import com.example.loo_zfeng_33533008.ui.screen.*
import com.example.loo_zfeng_33533008.ui.theme.Loo_Zfeng_33533008Theme
import com.example.loo_zfeng_33533008.ui.viewmodel.*
import com.example.loo_zfeng_33533008.ui.viewmodel.factory.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var patientRepository: PatientRepository
    private lateinit var userRepository: UserRepository
    private lateinit var motivationalMessageRepository: MotivationalMessageRepository

    private val navigationViewModel: NavigationViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init DB
        database = AppDatabase.getDatabase(applicationContext)
        patientRepository = PatientRepository(database.patientDao())
        userRepository = UserRepository(database.userDao())
        motivationalMessageRepository = MotivationalMessageRepository(database.motivationalMessageDao())

        // Reset database on launch (if needed)
        lifecycleScope.launch {
            DatabaseInitializer.resetInitFlag(applicationContext)
        }

        setContent {
            Loo_Zfeng_33533008Theme {
                AppNavigation(
                    authViewModel = authViewModel,
                    navigationViewModel = navigationViewModel,
                    patientRepository = patientRepository,
                    userRepository = userRepository,
                    motivationalMessageRepository = motivationalMessageRepository
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    navigationViewModel: NavigationViewModel,
    patientRepository: PatientRepository,
    motivationalMessageRepository: MotivationalMessageRepository,
    userRepository: UserRepository
) {
    val navigationState by authViewModel.navigationState.collectAsState()

    val showBottomBar = navigationState.route !in listOf(
        Screen.Login.route,
        Screen.Welcome.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MyBottomAppBar(
                    currentRoute = navigationState.route,
                    onNavigateTo = { route ->
                        authViewModel.navigateTo(
                            when (route) {
                                Screen.Home.route -> Screen.Home
                                Screen.Insights.route -> Screen.Insights
                                Screen.NutriCoach.route -> Screen.NutriCoach
                                Screen.Settings.route -> Screen.Settings
                                else -> return@MyBottomAppBar
                            }
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (navigationState.route) {
                Screen.Welcome.route -> WelcomeScreen(
                    onNavigateToLogin = { authViewModel.navigateTo(Screen.Login) }
                )
                Screen.Login.route -> LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { authViewModel.navigateTo(Screen.Home) }
                )
                Screen.Home.route -> HomeScreen()
                Screen.Insights.route -> InsightsScreen(
                    navigationViewModel = navigationViewModel,
                    onShare = {
                        // Optional: add analytics or feedback here
                    }
                )
                Screen.NutriCoach.route -> {
                    val nutriCoachViewModel: NutriCoachViewModel = viewModel(
                        factory = NutriCoachViewModelFactory(motivationalMessageRepository, authViewModel)
                    )
                    NutriCoachScreen(nutriCoachViewModel)
                }
                Screen.Settings.route -> SettingsScreen(
                    authViewModel = authViewModel,
                    onLogout = { authViewModel.logout() },
                    onClinicianLoginClick = { authViewModel.navigateTo(Screen.ClinicianLogin) }
                )
                Screen.ClinicianLogin.route -> ClinicianLoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { authViewModel.navigateTo(Screen.ClinicianDashboard) }
                )
                Screen.ClinicianDashboard.route -> {
                    val clinicianDashboardViewModel: ClinicianDashboardViewModel = viewModel(
                        factory = ClinicianDashboardViewModelFactory(patientRepository, userRepository)
                    )
                    ClinicianDashboardScreen(viewModel = clinicianDashboardViewModel)
                }
            }
        }
    }
}
