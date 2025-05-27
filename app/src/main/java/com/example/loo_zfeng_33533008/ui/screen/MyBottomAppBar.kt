package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.loo_zfeng_33533008.R
import com.example.loo_zfeng_33533008.ui.navigation.Screen

@Composable
fun MyBottomAppBar(
    currentRoute: String,
    onNavigateTo: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = { onNavigateTo(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.insight),
                    contentDescription = "Insights",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Insights") },
            selected = currentRoute == Screen.Insights.route,
            onClick = { onNavigateTo(Screen.Insights.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.nutricoach),
                    contentDescription = "NutriCoach",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("NutriCoach") },
            selected = currentRoute == Screen.NutriCoach.route,
            onClick = { onNavigateTo(Screen.NutriCoach.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == Screen.Settings.route,
            onClick = { onNavigateTo(Screen.Settings.route) }
        )
    }
} 