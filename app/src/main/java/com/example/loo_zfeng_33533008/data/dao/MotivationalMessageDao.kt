package com.example.loo_zfeng_33533008.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loo_zfeng_33533008.data.entity.MotivationalMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MotivationalMessageDao {
    @Query("SELECT * FROM motivational_messages WHERE userId = :userId ORDER BY timestamp DESC")
    fun getMessagesForUser(userId: String): Flow<List<MotivationalMessage>>

    @Insert
    suspend fun insertMessage(message: MotivationalMessage)
} 