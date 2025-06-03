package com.example.digiboxtest.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.database.AppDatabase
import com.example.digiboxtest.database.DatasetEntity
import com.example.digiboxtest.database.DatasetRepository
import kotlinx.coroutines.launch

class DatasetRoomViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).datasetDao()
    private val repo = DatasetRepository(dao)

    private val _datasetList = mutableStateListOf<DatasetEntity>()
    val datasetList: List<DatasetEntity> get() = _datasetList

    init {
        viewModelScope.launch {
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
        }
    }

    fun addDataset(dataset: DatasetEntity) {
        viewModelScope.launch {
            repo.insert(dataset)
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
        }
    }

    fun deleteDataset(dataset: DatasetEntity) {
        viewModelScope.launch {
            repo.delete(dataset)
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
        }
    }

    fun updateDataset(dataset: DatasetEntity) {
        viewModelScope.launch {
            repo.update(dataset)
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
        }
    }
}