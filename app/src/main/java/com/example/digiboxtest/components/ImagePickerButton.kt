package com.example.digiboxtest.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.digiboxtest.utils.saveBitmapToCache

@Composable
fun ImagePickerButton(
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current

    // Ambil dari kamera (bitmap -> uri)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        val uri = bitmap?.let {
            saveBitmapToCache(context, it) // Simpan bitmap ke cache dan dapatkan Uri
        }
        onImagePicked(uri)
    }

    // Ambil dari galeri
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
            onClick = { cameraLauncher.launch(null) }, // FIX: launch(null) untuk TakePicturePreview
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
        ) {
            Text("Ambil Foto", color = Color.White)
        }
    }
}
