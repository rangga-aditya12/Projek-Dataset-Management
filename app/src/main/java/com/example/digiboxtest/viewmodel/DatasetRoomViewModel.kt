package com.example.digiboxtest.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.database.AppDatabase
import com.example.digiboxtest.database.DatasetEntity
import com.example.digiboxtest.database.DatasetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatasetRoomViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).datasetDao()
    private val repo = DatasetRepository(dao)

    private val _datasetList = mutableStateListOf<DatasetEntity>()
    val datasetList: List<DatasetEntity> get() = _datasetList

    // --- State Sementara untuk Pembuatan Dataset ---
    var newDatasetTitle by mutableStateOf("")
    var newDatasetDescription by mutableStateOf("")
    var newDatasetCategory by mutableStateOf("")
    var newDatasetFormat by mutableStateOf("")
    var newDatasetCreator by mutableStateOf("")
    var newDatasetVerifier by mutableStateOf("")
    var newDatasetFileUri by mutableStateOf<Uri?>(null)
    var newDatasetProfileImageUri by mutableStateOf<Uri?>(null)
    var newDatasetIsPublic by mutableStateOf(false)
    // State baru untuk menampung hasil hitungan
    var newDatasetRowCount by mutableIntStateOf(0)
    var newDatasetFeatureCount by mutableIntStateOf(0)


    init {
        viewModelScope.launch {
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
        }
    }

    // --- FUNGSI BARU UNTUK MEMPROSES FILE CSV ---
    fun processCsvFile(uri: Uri) {
        // Jalankan di thread IO agar tidak memblokir UI
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Gunakan ContentResolver untuk membuka stream dari URI
                getApplication<Application>().contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = inputStream.bufferedReader()
                    // Baca baris pertama sebagai header untuk menghitung fitur
                    val header = reader.readLine()
                    val features = header?.split(',')?.size ?: 0

                    // Hitung sisa barisnya
                    val rows = reader.readLines().size

                    // Update state di thread utama
                    launch(Dispatchers.Main) {
                        newDatasetFeatureCount = features
                        newDatasetRowCount = rows
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Jika error, reset hitungan
                launch(Dispatchers.Main) {
                    newDatasetFeatureCount = 0
                    newDatasetRowCount = 0
                }
            }
        }
    }
    // ---------------------------------------------

    fun submitNewDataset() {
        viewModelScope.launch {
            val newEntity = DatasetEntity(
                title = newDatasetTitle,
                description = newDatasetDescription,
                lastUpdate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                // Gunakan hasil hitungan dari state
                rowCount = newDatasetRowCount,
                featureCount = newDatasetFeatureCount,
                keywords = newDatasetCategory,
                profileImageUri = newDatasetProfileImageUri?.toString(),
                category = newDatasetCategory,
                creator = newDatasetCreator,
                verifier = newDatasetVerifier
            )
            repo.insert(newEntity)
            _datasetList.clear()
            _datasetList.addAll(repo.getAll())
            clearCreationState()
        }
    }

    private fun clearCreationState() {
        newDatasetTitle = ""
        newDatasetDescription = ""
        newDatasetCategory = ""
        newDatasetFormat = ""
        newDatasetCreator = ""
        newDatasetVerifier = ""
        newDatasetFileUri = null
        newDatasetProfileImageUri = null
        newDatasetIsPublic = false
        // Reset juga state hitungan
        newDatasetRowCount = 0
        newDatasetFeatureCount = 0
    }

    // ... (sisa fungsi add, delete, update)
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