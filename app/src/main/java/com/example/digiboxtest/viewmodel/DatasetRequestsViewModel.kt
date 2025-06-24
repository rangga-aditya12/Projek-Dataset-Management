// File: app/src/main/java/com/example/digiboxtest/viewmodel/DatasetRequestsViewModel.kt
package com.example.digiboxtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.BuildConfig
import com.example.digiboxtest.ui.theme.DatasetRequest
import com.example.digiboxtest.ui.theme.DetailedDatasetRequest
import com.example.digiboxtest.ui.theme.mockRequests
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DatasetRequestsViewModel : ViewModel() {

    // State untuk daftar pesan/request.
    private val _requests = MutableStateFlow<List<DatasetRequest>>(emptyList())
    val requests = _requests.asStateFlow()

    // State untuk menampilkan pesan informasi di UI.
    private val _message = MutableStateFlow("Tekan tombol untuk mengambil pesan.")
    val message = _message.asStateFlow()

    // State untuk status loading.
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // StateFlow baru untuk menyimpan hasil detail dari API
    private val _selectedRequest = MutableStateFlow<DetailedDatasetRequest?>(null)
    val selectedRequest = _selectedRequest.asStateFlow()

    init {
        // Memuat data awal saat ViewModel pertama kali dibuat
        _requests.value = mockRequests
        _message.value = "Tidak ada pesan baru untuk ditambahkan (semua data sudah ada)."
    }

    /**
     * Fungsi ini dipanggil dari UI untuk mengambil data terbaru dari API.
     */
    fun fetchLatestRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = "Mengambil data terbaru..."

            try {
                // Menggunakan URL dari BuildConfig dan endpoint yang benar.
                val apiUrl = BuildConfig.DATASET_REQUEST_API_URL + "api-content/dataset-requests/"

                // --- SIMULASI PEMANGGILAN API ---
                // TODO: Ganti bagian ini dengan logika pemanggilan API sesungguhnya (misal: Retrofit/Ktor)
                _message.value = "Mengambil data dari: $apiUrl"
                delay(2000) // Simulasi jeda waktu jaringan

                // Ganti mockRequests dengan hasil parsing dari API
                val newRequestsFromApi = mockRequests
                _requests.value = newRequestsFromApi
                _message.value = "Data berhasil diperbarui (dari mock data)."

            } catch (e: Exception) {
                _message.value = "Gagal mengambil data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fungsi baru untuk mengambil detail dari API berdasarkan ID
     */
    fun fetchRequestDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedRequest.value = null // Kosongkan data sebelumnya

            try {
                // TODO: Ganti logika di bawah ini dengan pemanggilan API detail yang sesungguhnya.
                // Contoh: val apiUrl = BuildConfig.DATASET_REQUEST_API_URL + "api-content/dataset-requests/$id"

                // Untuk simulasi, kita pakai data palsu dengan jeda waktu
                delay(1500) // Simulasi waktu tunggu jaringan
                val detailsFromApi = getRequestDetails(id) // Menggunakan fungsi lama sebagai sumber data palsu

                _selectedRequest.value = detailsFromApi

            } catch (e: Exception) {
                _message.value = "Gagal mengambil detail: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fungsi ini sekarang hanya sebagai penyedia data palsu (mock) untuk simulasi.
     */
    private fun getRequestDetails(id: Int): DetailedDatasetRequest {
        return DetailedDatasetRequest(
            id = id,
            projectName = "web manajemen proyek",
            problemDescription = "Membuat aplikasi Manajemen Proyek berbasis django untuk melengkapi tugas semester 4",
            target = "biar lulus semester 4 aja",
            dataType = "data log",
            processingActivity = "normalisasi",
            featureCount = 4,
            datasetSize = 1,
            fileFormat = "CSV",
            startDate = "Feb. 9, 2025",
            endDate = "Aug. 29, 2028",
            status = "Complete"
        )
    }

    /**
     * Fungsi ini bisa dipanggil dari UI untuk membalas pesan.
     * (Implementasi tidak diubah)
     */
    fun replyToRequest(requestId: Int, replyMessage: String) {
        viewModelScope.launch {
            _message.value = "Mengirim balasan untuk request #$requestId..."
            _isLoading.value = true
            try {
                delay(1500)
                _message.value = "Balasan berhasil terkirim."
            } catch (e: Exception) {
                _message.value = "Gagal mengirim balasan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}