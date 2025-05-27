package com.example.loo_zfeng_33533008.data.dao

import androidx.room.*
import com.example.loo_zfeng_33533008.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId AND password = :password")
    suspend fun validateUser(userId: String, password: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE userId = :userId)")
    suspend fun isUserRegistered(userId: String): Boolean
} 