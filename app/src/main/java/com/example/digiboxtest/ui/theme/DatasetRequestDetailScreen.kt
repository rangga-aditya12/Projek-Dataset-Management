package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DatasetRequestDetailScreen(
    navController: NavController,
    request: DetailedDatasetRequest
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4F5F8)) // Latar belakang biru muda
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
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

                DetailRow(label = "Nama Proyek:", value = request.projectName)
                DetailRow(label = "Deskripsi Masalah:", value = request.problemDescription)
                DetailRow(label = "Target:", value = request.target)
                DetailRow(label = "Tipe Data:", value = request.dataType)
                DetailRow(label = "Aktivitas Pemrosesan:", value = request.processingActivity)
                DetailRow(label = "Jumlah Fitur:", value = request.featureCount.toString())
                DetailRow(label = "Ukuran Dataset:", value = request.datasetSize.toString())
                DetailRow(label = "Format File:", value = request.fileFormat)
                DetailRow(label = "Tanggal Mulai:", value = request.startDate)
                DetailRow(label = "Tanggal Selesai:", value = request.endDate)
                DetailRow(label = "Status:", value = request.status)

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