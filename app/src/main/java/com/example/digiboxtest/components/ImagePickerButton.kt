package com.example.digiboxtest.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.digiboxtest.utils.saveBitmapToCache

@Composable
fun ImagePickerButton(
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current

    // Launcher untuk mengambil gambar dari kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        val uri = bitmap?.let {
            saveBitmapToCache(context, it) // Simpan bitmap ke cache dan dapatkan Uri
        }
        onImagePicked(uri)
    }

    // Launcher untuk meminta izin kamera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Jika izin diberikan, luncurkan kamera
            cameraLauncher.launch(null)
        } else {
            // Handle jika izin ditolak (misalnya, tampilkan pesan)
            // Untuk saat ini, tidak melakukan apa-apa
        }
    }

    // Launcher untuk mengambil gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImagePicked(uri)
    }

    Column {
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        ) {
            Text("Pilih dari Galeri", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                // Periksa izin kamera sebelum meluncurkan
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                        // Jika izin sudah ada, langsung luncurkan kamera
                        cameraLauncher.launch(null)
                    }
                    else -> {
                        // Jika belum ada izin, minta izin terlebih dahulu
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
        ) {
            Text("Ambil Foto", color = Color.White)
        }
    }
}