package com.example.digiboxtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatasetEntity::class, UserEntity::class], version = 5) // Versi dinaikkan, UserEntity ditambahkan
abstract class AppDatabase : RoomDatabase() {
    abstract fun datasetDao(): DatasetDao
    abstract fun userDao(): UserDao // Tambahkan abstract function untuk UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "digibox_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}