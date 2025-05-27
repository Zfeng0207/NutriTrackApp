package com.example.loo_zfeng_33533008.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.loo_zfeng_33533008.data.dao.FoodIntakeDao
import com.example.loo_zfeng_33533008.data.dao.PatientDao
import com.example.loo_zfeng_33533008.data.dao.MotivationalMessageDao
import com.example.loo_zfeng_33533008.data.dao.UserDao
import com.example.loo_zfeng_33533008.data.entity.FoodIntake
import com.example.loo_zfeng_33533008.data.entity.Patient
import com.example.loo_zfeng_33533008.data.entity.User
import com.example.loo_zfeng_33533008.data.entity.MotivationalMessage
import com.example.loo_zfeng_33533008.data.repository.PatientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Patient::class, FoodIntake::class, User::class, MotivationalMessage::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun userDao(): UserDao
    abstract fun motivationalMessageDao(): MotivationalMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val TAG = "AppDatabase"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Log.d(TAG, "Creating database instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "Database created, triggering initialization")
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val initializer = DatabaseInitializer(
                                    context.applicationContext,
                                    PatientRepository(getDatabase(context).patientDao())
                                )
                                initializer.initializeIfNeeded()
                            } catch (e: Exception) {
                                Log.e(TAG, "Error during database initialization", e)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                Log.d(TAG, "Database instance created and stored")
                instance
            }
        }
    }
}

 