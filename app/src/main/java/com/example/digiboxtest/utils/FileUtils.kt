package com.example.digiboxtest.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Menyalin file dari URI sumber (misalnya dari file picker) ke penyimpanan internal aplikasi.
 * @return Mengembalikan URI dari file baru yang sudah disalin di penyimpanan internal.
 * Ini adalah URI yang permanen dan bisa disimpan di database.
 */
fun copyFileToInternalStorage(context: Context, sourceUri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(sourceUri)
        // Buat nama file unik untuk menghindari konflik
        val fileName = "dataset_${UUID.randomUUID()}.csv"
        // Menggunakan context.filesDir untuk penyimpanan internal yang privat dan permanen
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        // Mengembalikan URI dari file yang baru dibuat di folder internal
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}