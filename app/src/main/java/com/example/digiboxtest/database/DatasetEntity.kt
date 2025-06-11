package com.example.digiboxtest.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataset_table")
data class DatasetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val lastUpdate: String,
    val rowCount: Int,
    val featureCount: Int,
    val keywords: String,
    val profileImageUri: String? = null
)