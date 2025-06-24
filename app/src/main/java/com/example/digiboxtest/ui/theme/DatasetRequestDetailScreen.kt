// File: app/src/main/java/com/example/digiboxtest/ui/theme/DatasetRequestDetailScreen.kt
package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digiboxtest.viewmodel.DatasetRequestsViewModel

@Composable
fun DatasetRequestDetailScreen(
    navController: NavController,
    viewModel: DatasetRequestsViewModel // Terima ViewModel, bukan 'request'
) {
    // Ambil state dari ViewModel
    val request by viewModel.selectedRequest.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4F5F8))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tampilkan indikator loading jika sedang mengambil data
        if (isLoading) {
            CircularProgressIndicator()
        }
        // Jika data sudah ada (tidak null), tampilkan Card detail
        else if (request != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Detail Permintaan Dataset",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 24.dp)
                    )

                    // Gunakan data dari state 'request'
                    DetailRow(label = "Nama Proyek:", value = request!!.projectName)
                    DetailRow(label = "Deskripsi Masalah:", value = request!!.problemDescription)
                    DetailRow(label = "Target:", value = request!!.target)
                    DetailRow(label = "Tipe Data:", value = request!!.dataType)
                    DetailRow(label = "Aktivitas Pemrosesan:", value = request!!.processingActivity)
                    DetailRow(label = "Jumlah Fitur:", value = request!!.featureCount.toString())
                    DetailRow(label = "Ukuran Dataset:", value = request!!.datasetSize.toString())
                    DetailRow(label = "Format File:", value = request!!.fileFormat)
                    DetailRow(label = "Tanggal Mulai:", value = request!!.startDate)
                    DetailRow(label = "Tanggal Selesai:", value = request!!.endDate)
                    DetailRow(label = "Status:", value = request!!.status)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Kembali")
                    }
                }
            }
        }
        // Jika tidak loading dan data null (misal: terjadi error)
        else {
            Text("Gagal memuat detail permintaan.")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(150.dp)
        )
        Text(text = value, modifier = Modifier.weight(1f))
    }
}