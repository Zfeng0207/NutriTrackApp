package com.example.loo_zfeng_33533008.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loo_zfeng_33533008.ui.viewmodel.ClinicianDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicianDashboardScreen(
    viewModel: ClinicianDashboardViewModel
) {
    val patterns by viewModel.patterns.collectAsState()
    val showPatterns by viewModel.showPatterns.collectAsState()
    val avgHeifaMale by viewModel.avgHeifaMale.collectAsState()
    val avgHeifaFemale by viewModel.avgHeifaFemale.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Average HEIFA Total Scores:",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        ClinicianStatRow(label = "Average HEIFA (Male)", value = avgHeifaMale)
        ClinicianStatRow(label = "Average HEIFA (Female)", value = avgHeifaFemale)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.findDataPatterns("AIzaSyCfUKwG3rjmAWYQL9ZKwww8nWdtOC_au10")
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Find Data Pattern", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showPatterns) {
            Column(modifier = Modifier.fillMaxWidth()) {
                patterns.forEachIndexed { index, insight ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Insight ${index + 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = insight.trim(),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }



        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: Navigation back or finish */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Done, contentDescription = "Done", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Done", color = Color.White)
        }
    }
}

@Composable
fun ClinicianStatRow(label: String, value: Double) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
        Text(":", Modifier.padding(horizontal = 8.dp))
        Text(value.toString(), fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}
