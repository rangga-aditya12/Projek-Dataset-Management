package com.example.digiboxtest.ui.theme

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.components.ImagePickerButton
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

// Fungsi bantuan untuk mendapatkan nama file dari URI dengan lebih andal
private fun getFileName(uri: Uri, context: Context): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = cursor.getString(displayNameIndex)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result ?: "Unknown file"
}


@Composable
fun DatasetMetadataScreen(
    navController: NavController,
    viewModel: DatasetRoomViewModel
) {
    val context = LocalContext.current // Dapatkan context untuk digunakan di helper
    val bgGradient = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color(0xFFA1D4CA), Color.White)
    )

    var datasetFileName by remember { mutableStateOf("No file chosen") }
    var profileImageFileName by remember { mutableStateOf("No file chosen") }

    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.newDatasetFileUri = it
            datasetFileName = getFileName(it, context) // Gunakan helper untuk nama file
            viewModel.processCsvFile(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "DigiBox Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Mulai Proyek Anda dengan Dataset Baru",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D124B)
            )
            Text(
                "Lengkapi detail dan unggah file yang diperlukan untuk membuat dataset baru Anda.",
                fontSize = 13.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // 1. File Dataset Picker
                Text("1. File Dataset (hanya .CSV)", fontSize = 14.sp)
                // --- PERUBAHAN DI SINI ---
                // Mengizinkan semua jenis file agar CSV pasti bisa dipilih
                Button(onClick = { csvPickerLauncher.launch("*/*") }) {
                    Text("Choose File")
                }
                Text(datasetFileName, fontSize = 12.sp, color = Color.Gray)

                if (viewModel.newDatasetRowCount > 0 || viewModel.newDatasetFeatureCount > 0) {
                    Text(
                        "Detected: ${viewModel.newDatasetRowCount} rows, ${viewModel.newDatasetFeatureCount} features",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0D47A1),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Profil Dataset Picker
                Text("2. Profil Dataset (Gambar)", fontSize = 14.sp)
                ImagePickerButton(onImagePicked = { uri ->
                    uri?.let {
                        viewModel.newDatasetProfileImageUri = it
                        profileImageFileName = getFileName(it, context)
                    }
                })

                viewModel.newDatasetProfileImageUri?.let {
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Profile Image Preview",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Jadikan Publik Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.newDatasetIsPublic,
                        onCheckedChange = { viewModel.newDatasetIsPublic = it }
                    )
                    Text("Jadikan dataset ini publik (bisa dilihat semua user)")
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Kembali", color = Color.White)
                }

                Button(
                    onClick = {
                        viewModel.submitNewDataset()
                        navController.navigate("datasetList") {
                            popUpTo("home")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D124B))
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }
    }
}