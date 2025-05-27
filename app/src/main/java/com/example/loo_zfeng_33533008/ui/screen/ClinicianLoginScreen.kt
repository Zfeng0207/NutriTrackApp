package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loo_zfeng_33533008.ui.navigation.Screen
import com.example.loo_zfeng_33533008.ui.viewmodel.AuthViewModel

@Composable
fun ClinicianLoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var clinicianKey by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Login",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 24.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = clinicianKey,
            onValueChange = { 
                clinicianKey = it
                showError = false // Clear error when user types
            },
            label = { Text("Clinician Key", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            placeholder = { Text("Enter your clinician key", fontSize = 18.sp, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            isError = showError
        )

        if (showError) {
            Text(
                text = "Invalid clinician key",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                if (clinicianKey == "dollar-entry-apples") {
                    authViewModel.navigateTo(Screen.ClinicianDashboard)
                    onLoginSuccess()
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F22D9)),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Filled.ArrowForward, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("Clinician Login", fontSize = 20.sp)
        }
    }
} 