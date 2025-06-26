package com.example.digiboxtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.network.RetrofitInstance
import com.example.digiboxtest.ui.theme.DatasetRequest
import com.example.digiboxtest.ui.theme.DetailedDatasetRequest
import com.example.digiboxtest.ui.theme.ReplyRequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DatasetRequestsViewModel : ViewModel() {

    private val _requests = MutableStateFlow<List<DatasetRequest>>(emptyList())
    val requests = _requests.asStateFlow()

    private val _message = MutableStateFlow("Tekan tombol untuk mengambil pesan.")
    val message = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedRequest = MutableStateFlow<DetailedDatasetRequest?>(null)
    val selectedRequest = _selectedRequest.asStateFlow()

    init {
        fetchLatestRequests()
    }

    fun fetchLatestRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = "Mengambil data terbaru dari server..."
            try {
                val response = RetrofitInstance.api.getDatasetRequests()
                if (response.isSuccessful && response.body() != null) {
                    _requests.value = response.body()!!
                    _message.value = "Data berhasil diperbarui."
                } else {
                    _message.value = "Gagal mengambil data: ${response.message()}"
                }
            } catch (e: HttpException) {
                _message.value = "Gagal mengambil data: Terjadi masalah pada server."
            } catch (e: IOException) {
                _message.value = "Gagal mengambil data: Periksa koneksi internet Anda."
            } catch (e: Exception) {
                _message.value = "Gagal mengambil data: Terjadi kesalahan tidak terduga."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRequestDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedRequest.value = null
            try {
                val response = RetrofitInstance.api.getDatasetRequestDetail(id)
                if (response.isSuccessful && response.body() != null) {
                    _selectedRequest.value = response.body()
                } else {
                    _message.value = "Gagal mengambil detail: ${response.message()}"
                }
            } catch (e: HttpException) {
                _message.value = "Gagal mengambil detail: Terjadi masalah pada server."
            } catch (e: IOException) {
                _message.value = "Gagal mengambil detail: Periksa koneksi internet Anda."
            } catch (e: Exception) {
                _message.value = "Gagal mengambil detail: Terjadi kesalahan tidak terduga."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Mengirim balasan ke API. Dibuat sebagai 'suspend fun' yang mengembalikan Boolean
     * untuk memberitahu UI apakah prosesnya berhasil atau tidak.
     */
    suspend fun sendReply(
        replyUrl: String,
        message: String,
        downloadLink: String,
        status: String
    ): Boolean {
        _message.value = "Mengirim balasan..."
        _isLoading.value = true
        val requestBody = ReplyRequestBody(
            replyMessage = message,
            downloadLink = downloadLink,
            status = status
        )
        return try {
            val response = RetrofitInstance.api.sendReply(replyUrl, requestBody)
            if (response.isSuccessful) {
                _message.value = "Balasan berhasil terkirim."
                true // Kembalikan true jika sukses
            } else {
                _message.value = "Gagal mengirim balasan: Server merespons dengan kode ${response.code()}"
                false // Kembalikan false jika gagal
            }
        } catch (e: Exception) {
            _message.value = "Gagal mengirim balasan: Periksa koneksi internet Anda."
            e.printStackTrace()
            false // Kembalikan false jika ada error
        } finally {
            _isLoading.value = false
        }
    }
}