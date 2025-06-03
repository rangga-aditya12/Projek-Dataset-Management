package com.example.digiboxtest.database

class DatasetRepository(private val dao: DatasetDao) {
    suspend fun getAll() = dao.getAll()
    suspend fun insert(dataset: DatasetEntity) = dao.insert(dataset)
    suspend fun delete(dataset: DatasetEntity) = dao.delete(dataset)
    suspend fun update(dataset: DatasetEntity) = dao.update(dataset)
}