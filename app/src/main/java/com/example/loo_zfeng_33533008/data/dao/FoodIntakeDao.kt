package com.example.loo_zfeng_33533008.data.dao

import androidx.room.*
import com.example.loo_zfeng_33533008.data.entity.FoodIntake
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodIntakeDao {
    @Query("SELECT * FROM food_intakes WHERE patientId = :patientId ORDER BY timestamp DESC")
    fun getFoodIntakesForPatient(patientId: String): Flow<List<FoodIntake>>

    @Insert
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Update
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Delete
    suspend fun deleteFoodIntake(foodIntake: FoodIntake)

    @Query("DELETE FROM food_intakes WHERE patientId = :patientId")
    suspend fun deleteAllFoodIntakesForPatient(patientId: String)
} 