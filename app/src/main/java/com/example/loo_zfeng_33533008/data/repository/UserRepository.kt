package com.example.loo_zfeng_33533008.data.repository

import com.example.loo_zfeng_33533008.data.dao.UserDao
import com.example.loo_zfeng_33533008.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getUserById(userId: String): Flow<User?> = userDao.getUserById(userId)

    suspend fun registerUser(userId: String, phoneNumber: String, password: String) {
        val user = User(userId, phoneNumber, password)
        userDao.insertUser(user)
    }

    suspend fun validateUser(userId: String, password: String): User? {
        return userDao.validateUser(userId, password)
    }

    suspend fun isUserRegistered(userId: String): Boolean {
        return userDao.isUserRegistered(userId)
    }
} 