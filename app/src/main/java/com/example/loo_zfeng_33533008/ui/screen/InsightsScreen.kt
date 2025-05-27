package com.example.loo_zfeng_33533008.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loo_zfeng_33533008.ui.viewmodel.NavigationViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun ShareButtonWithLogic(shareText: String, onShare: () -> Unit = {}) {
    val context = LocalContext.current

    Button(
        onClick = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share insights via"))
            onShare()
        },
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text("Share Insights")
    }
}

@Composable
fun InsightsScreen(navigationViewModel: NavigationViewModel, onShare: () -> Unit) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getString("userId", "") ?: ""
    val phoneNumber = sharedPref.getString("phoneNumber", "") ?: ""

    val sliderValues = remember {
        mutableStateMapOf(
            "Vegetables" to 80f,
            "Fruits" to 70f,
            "Grains" to 90f,
            "Whole Grains" to 65f,
            "Meat & Alternatives" to 75f,
            "Dairy" to 60f,
            "Water" to 85f,
            "Unsaturated Fats" to 55f,
            "Sodium" to 45f,
            "Sugar" to 50f,
            "Alcohol" to 30f,
            "Discretionary" to 40f
        )
    }

    val totalScore = remember { calculateTotalScore(context, "user_data.csv", userId, phoneNumber) }

    val insightsTextToShare = buildString {
        append("🍽️ My NutriTrack Insights:\n\n")
        sliderValues.forEach { (category, value) ->
            append("- $category: ${value.toInt()}%\n")
        }
        append("\nTotal Score: ${totalScore.toInt()}/100 🎯")
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Food Quality Insights",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            sliderValues.forEach { (category, sliderValue) ->
                Column(modifier = Modifier.padding(vertical = 12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "${sliderValue.toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp
                        )
                    }
                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValues[category] = it },
                        valueRange = 0f..100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(thickness = 0.5.dp)

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    "Total Score: $totalScore/100",
                    style = MaterialTheme.typography.titleMedium
                )
                LinearProgressIndicator(
                    progress = totalScore / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = when {
                        totalScore >= 80 -> Color(0xFF4CAF50)
                        totalScore >= 60 -> Color(0xFFFFC107)
                        else -> Color(0xFFF44336)
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    ShareButtonWithLogic(
                        shareText = insightsTextToShare,
                        onShare = onShare
                    )
                }

                Button(
                    onClick = { navigationViewModel.navigateTo("nutricoach") },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Diet Tips", fontSize = 12.sp)
                }
            }
        }
    }
}

fun calculateTotalScore(context: Context, fileName: String, userId: String, phoneNumber: String): Float {
    val assets = context.assets
    return try {
        val inputStream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var totalScore = 0f

        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val values = line.split(",")
                if (values.size > 4 &&
                    values[1].trim() == userId &&
                    values[0].trim() == phoneNumber) {

                    totalScore = if (values[2].trim().equals("Male", ignoreCase = true)) {
                        values[3].toFloatOrNull() ?: 0f
                    } else {
                        values[4].toFloatOrNull() ?: 0f
                    }
                    return@useLines
                }
            }
        }
        totalScore
    } catch (e: Exception) {
        0f
    }
}
