// File: app/src/main/java/com/example/digiboxtest/viewmodel/DatasetRequestsViewModel.kt
package com.example.digiboxtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.BuildConfig
import com.example.digiboxtest.ui.theme.DatasetRequest
import com.example.digiboxtest.ui.theme.DetailedDatasetRequest // <-- Tambahkan import ini
import com.example.digiboxtest.ui.theme.mockRequests
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DatasetRequestsViewModel : ViewModel() {

    // State untuk daftar pesan/request. _requests bersifat privat dan bisa diubah.
    private val _requests = MutableStateFlow<List<DatasetRequest>>(emptyList())
    // requests bersifat publik dan hanya bisa dibaca (read-only) oleh UI.
    val requests = _requests.asStateFlow()

    // State untuk menampilkan pesan informasi di UI.
    private val _message = MutableStateFlow("Tekan tombol untuk mengambil pesan.")
    val message = _message.asStateFlow()

    // State untuk status loading (menampilkan/menyembunyikan progress indicator).
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        // Memuat data awal saat ViewModel pertama kali dibuat
        _requests.value = mockRequests
        _message.value = "Tidak ada pesan baru untuk ditambahkan (semua data sudah ada)."
    }

    /**
     * Fungsi ini dipanggil dari UI untuk mengambil data terbaru dari API.
     */
    fun fetchLatestRequests() {
        // viewModelScope akan otomatis membatalkan coroutine ini jika ViewModel dihancurkan.
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = "Mengambil data terbaru..."

            try {
                // Menggunakan URL dari BuildConfig yang sudah kita atur sebelumnya.
                val apiUrl = BuildConfig.DATASET_REQUEST_API_URL + "requests" // contoh endpoint

                // --- SIMULASI PEMANGGILAN API ---
                // TODO: Ganti bagian `delay` ini dengan logika pemanggilan API sesungguhnya
                // menggunakan library seperti Retrofit atau Ktor.
                delay(2000) // Simulasi jeda waktu jaringan

                // Contoh hasil sukses:
                // Misalkan API mengembalikan daftar request yang sama atau baru.
                val newRequestsFromApi = mockRequests // Ganti dengan hasil dari API
                _requests.value = newRequestsFromApi
                _message.value = "Data berhasil diperbarui."

            } catch (e: Exception) {
                // Menangani jika terjadi error saat memanggil API
                _message.value = "Gagal mengambil data: ${e.message}"
            } finally {
                // Pastikan status loading kembali ke false setelah selesai (baik sukses maupun gagal)
                _isLoading.value = false
            }
        }
    }

    /**
     * Fungsi ini bisa dipanggil dari UI untuk membalas pesan.
     * @param requestId ID dari request yang akan dibalas.
     * @param replyMessage Isi balasan.
     */
    fun replyToRequest(requestId: Int, replyMessage: String) {
        viewModelScope.launch {
            _message.value = "Mengirim balasan untuk request #$requestId..."
            _isLoading.value = true

            try {
                val apiUrl = BuildConfig.DATASET_REQUEST_API_URL + "reply" // contoh endpoint

                // --- SIMULASI PEMANGGILAN API UNTUK REPLY ---
                // TODO: Implementasikan logika untuk mengirim data balasan ke API di sini.
                delay(1500)

                _message.value = "Balasan berhasil terkirim."

            } catch (e: Exception) {
                _message.value = "Gagal mengirim balasan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * [BARU] Fungsi untuk mendapatkan detail permintaan palsu berdasarkan ID.
     * Di aplikasi nyata, ini akan mengambil data dari API atau database.
     */
    fun getRequestDetails(id: Int): DetailedDatasetRequest {
        // Data palsu untuk demonstrasi
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
}