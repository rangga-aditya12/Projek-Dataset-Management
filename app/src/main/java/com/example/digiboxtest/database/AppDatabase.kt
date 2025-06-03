package com.example.digiboxtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatasetEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun datasetDao(): DatasetDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "digibox_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
