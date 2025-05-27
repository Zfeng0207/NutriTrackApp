package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loo_zfeng_33533008.R // Assuming your logo is in drawable and referenced via R
import com.example.loo_zfeng_33533008.ui.navigation.Screen // Assuming Screen sealed class is defined here

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "NutriTrack",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Assuming you have a logo in your drawable resources named 'nutritrack_logo'
        Image(
            painter = painterResource(id = R.drawable.nutritrack_logo),
            contentDescription = "NutriTrack Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. Use this app at your own risk.\n\nIf you\'d like to an Accredited Practicing Dietitian (APD),\nplease visit the Monash Nutrition/Dietetics Clinic\n(discounted rates for students):\nhttps://www.monash.edu/medicine/scs/nutrition/clinics/\nnutrition",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F22D9)),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text("Login", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Designed with ❤️ by Alex Scott (14578373)",
            style = MaterialTheme.typography.bodySmall
        )
    }
} 