package com.example.digiboxtest.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

// Menambahkan anotasi untuk menekan peringatan Deprecation yang disengaja
@Suppress("DEPRECATION")
fun downloadCsvFile(context: Context, fileUriString: String?, title: String) {
    if (fileUriString == null) {
        Toast.makeText(context, "File location not available.", Toast.LENGTH_SHORT).show()
        return
    }

    val contentUri = Uri.parse(fileUriString)
    val fileName = "$title.csv"

    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null

    try {
        inputStream = context.contentResolver.openInputStream(contentUri)
        if (inputStream == null) {
            throw Exception("Failed to open input stream from URI.")
        }

        // Memeriksa versi Android untuk menentukan cara penyimpanan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Metode Modern untuk Android 10 (API 29) ke atas
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val destinationUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw Exception("Failed to create new file in Downloads.")
            outputStream = resolver.openOutputStream(destinationUri)

        } else {
            // Metode Lama untuk Android 9 (API 28) ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            // Membuat direktori jika belum ada
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            val file = File(downloadsDir, fileName)
            outputStream = FileOutputStream(file)
        }

        if (outputStream == null) {
            throw Exception("Failed to open output stream.")
        }

        // Salin data dari input ke output, logika ini sama untuk kedua metode
        inputStream.copyTo(outputStream)

        Toast.makeText(context, "File saved to Downloads folder.", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        // Pastikan stream selalu ditutup
        inputStream?.close()
        outputStream?.close()
    }
}