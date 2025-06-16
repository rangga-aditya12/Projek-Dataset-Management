package com.example.digiboxtest.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Menyalin file dari URI (misalnya dari galeri) ke penyimpanan internal permanen aplikasi.
 * @return Mengembalikan URI dari file baru yang sudah disalin di penyimpanan internal.
 */
fun copyUriToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        // Menggunakan context.filesDir untuk penyimpanan permanen
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Menyimpan Bitmap (dari kamera) ke penyimpanan internal permanen aplikasi.
 * @return Mengembalikan URI dari file baru yang sudah disimpan di penyimpanan internal.
 */
fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
        // Menggunakan context.filesDir untuk penyimpanan permanen
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
        }
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}