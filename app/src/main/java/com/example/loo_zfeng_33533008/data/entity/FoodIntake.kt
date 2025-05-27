package com.example.loo_zfeng_33533008.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intakes",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["userId"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val responses: String  // Store questionnaire responses as JSON string
) 