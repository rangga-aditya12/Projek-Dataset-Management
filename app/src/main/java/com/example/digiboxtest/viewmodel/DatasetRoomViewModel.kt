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
import com.example.digiboxtest.utils.copyFileToInternalStorage
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

    // --- FUNGSI UNTUK MEMPROSES FILE CSV ---
    fun processCsvFile(uri: Uri) {
        // Langsung salin file ke penyimpanan internal untuk mendapatkan URI permanen
        val permanentUri = copyFileToInternalStorage(getApplication(), uri)
        if (permanentUri == null) {
            // TODO: Handle error, misalnya dengan menampilkan Toast atau pesan di UI
            println("Error: Failed to copy file to internal storage.")
            return
        }
        // Simpan URI permanen dari salinan file internal ke dalam state
        newDatasetFileUri = permanentUri

        // Jalankan di thread IO untuk membaca metadata file (jumlah baris/fitur)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Gunakan URI asli untuk membaca metadata
                getApplication<Application>().contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = inputStream.bufferedReader()
                    val header = reader.readLine()
                    val features = header?.split(',')?.size ?: 0
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

    fun submitNewDataset() {
        viewModelScope.launch {
            val newEntity = DatasetEntity(
                title = newDatasetTitle,
                description = newDatasetDescription,
                lastUpdate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                rowCount = newDatasetRowCount,
                featureCount = newDatasetFeatureCount,
                keywords = newDatasetCategory,
                profileImageUri = newDatasetProfileImageUri?.toString(),
                category = newDatasetCategory,
                creator = newDatasetCreator,
                verifier = newDatasetVerifier,
                // Pastikan URI yang disimpan adalah URI permanen
                fileUri = newDatasetFileUri?.toString()
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
        newDatasetRowCount = 0
        newDatasetFeatureCount = 0
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