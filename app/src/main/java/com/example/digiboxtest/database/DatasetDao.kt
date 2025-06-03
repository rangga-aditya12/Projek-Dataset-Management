package com.example.digiboxtest.database

import androidx.room.*

@Dao
interface DatasetDao {
    @Query("SELECT * FROM dataset_table")
    suspend fun getAll(): List<DatasetEntity>

    @Insert
    suspend fun insert(dataset: DatasetEntity)

    @Delete
    suspend fun delete(dataset: DatasetEntity)

    @Update
    suspend fun update(dataset: DatasetEntity)
}