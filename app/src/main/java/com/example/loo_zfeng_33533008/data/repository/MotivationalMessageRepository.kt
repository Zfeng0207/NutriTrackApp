package com.example.loo_zfeng_33533008.data.repository

import com.example.loo_zfeng_33533008.data.dao.MotivationalMessageDao
import com.example.loo_zfeng_33533008.data.entity.MotivationalMessage
 
class MotivationalMessageRepository(private val dao: MotivationalMessageDao) {
    fun getMessagesForUser(userId: String) = dao.getMessagesForUser(userId)
    suspend fun insertMessage(message: MotivationalMessage) = dao.insertMessage(message)
} 