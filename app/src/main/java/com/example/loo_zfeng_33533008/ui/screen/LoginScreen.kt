package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.loo_zfeng_33533008.ui.viewmodel.AuthState
import com.example.loo_zfeng_33533008.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    var userId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isFirstTimeLogin by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val registrationSuccess by authViewModel.registrationSuccess.collectAsState()

    LaunchedEffect(authState, registrationSuccess) {
        when {
            registrationSuccess == true -> {
                // Reset form and switch to login mode
                userId = ""
                phoneNumber = ""
                password = ""
                confirmPassword = ""
                isFirstTimeLogin = false
                // Reset auth state to allow new login
                authViewModel.logout()
            }
            authState is AuthState.Authenticated -> {
                onLoginSuccess()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (registrationSuccess == true) {
            AlertDialog(
                onDismissRequest = { /* Dialog will be dismissed when state is reset */ },
                title = { Text("Success") },
                text = { Text("Registration successful! Please login with your credentials.") },
                confirmButton = {
                    TextButton(onClick = { /* Dialog will be dismissed when state is reset */ }) {
                        Text("OK")
                    }
                }
            )
        }

        Text(
            text = if (isFirstTimeLogin) "First Time Registration" else "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isFirstTimeLogin) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (password != confirmPassword && confirmPassword.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isFirstTimeLogin) {
                    if (password == confirmPassword) {
                        authViewModel.registerUser(userId, phoneNumber, password)
                    }
                } else {
                    authViewModel.login(userId, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = if (isFirstTimeLogin) {
                userId.isNotBlank() && phoneNumber.isNotBlank() && 
                password.isNotBlank() && password == confirmPassword
            } else {
                userId.isNotBlank() && password.isNotBlank()
            }
        ) {
            Text(if (isFirstTimeLogin) "Register" else "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { isFirstTimeLogin = !isFirstTimeLogin }
        ) {
            Text(if (isFirstTimeLogin) "Already registered? Login" else "First time? Register")
        }

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (authState is AuthState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
} 