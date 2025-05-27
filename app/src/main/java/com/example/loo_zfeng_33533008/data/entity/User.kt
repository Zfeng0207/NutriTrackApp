package com.example.loo_zfeng_33533008.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String,
    val phoneNumber: String,
    val password: String
)

@Entity(tableName = "motivational_messages")
data class MotivationalMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
) 