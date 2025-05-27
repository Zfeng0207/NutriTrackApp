package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loo_zfeng_33533008.ui.viewmodel.NutriCoachViewModel

@Composable
fun NutriCoachScreen(
    viewModel: NutriCoachViewModel
) {
    var fruitName by remember { mutableStateOf("") }
    val fruitDetails by viewModel.fruitDetails.collectAsState()
    val messages by viewModel.messages.collectAsState()
    var showTipsDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "NutriCoach",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = fruitName,
                onValueChange = { fruitName = it },
                label = { Text("Fruit Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                viewModel.fetchFruitDetails(fruitName.trim().lowercase())
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Details")
                Spacer(Modifier.width(4.dp))
                Text("Details")
            }
        }

        Spacer(Modifier.height(24.dp))

        if (fruitDetails != null) {
            Column(Modifier.fillMaxWidth()) {
                NutriCoachDetailRow("family", fruitDetails!!.family)
                NutriCoachDetailRow("calories", fruitDetails!!.calories.toString())
                NutriCoachDetailRow("fat", fruitDetails!!.fat.toString())
                NutriCoachDetailRow("sugar", fruitDetails!!.sugar.toString())
                NutriCoachDetailRow("carbohydrates", fruitDetails!!.carbohydrates.toString())
                NutriCoachDetailRow("protein", fruitDetails!!.protein.toString())
            }
        } else if (fruitName.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(
                "No nutrition data found for \"$fruitName\".",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.generateAndSaveMotivationalMessage(apiKey = "AIzaSyCfUKwG3rjmAWYQL9ZKwww8nWdtOC_au10")
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Message, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Motivational Message (AI)")
        }

        messages.firstOrNull()?.let {
            Text(it.message, Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { showTipsDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Shows All Tips")
        }

        if (showTipsDialog) {
            AlertDialog(
                onDismissRequest = { showTipsDialog = false },
                title = { Text("All Motivational Tips") },
                text = {
                    Column {
                        messages.forEach { msg ->
                            Text(msg.message)
                            Divider()
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTipsDialog = false }) { Text("Close") }
                }
            )
        }
    }
}

@Composable
fun NutriCoachDetailRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, Modifier.weight(1f))
        Text(":", Modifier.padding(horizontal = 8.dp))
        Text(value, Modifier.weight(1f))
    }
}
