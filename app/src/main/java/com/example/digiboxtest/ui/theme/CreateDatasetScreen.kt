package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digiboxtest.R
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@Composable
fun CreateDatasetScreen(navController: NavController, viewModel: DatasetRoomViewModel) {
    val bgGradient = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color(0xFFA1D4CA), Color.White)
    )

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
                "Buat dataset baru untuk dianalisis, dibagikan, atau digunakan dalam model Anda dengan mudah dan efisien.",
                fontSize = 13.sp,
                color = Color.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                DatasetTextField(
                    label = "1. Nama Dataset",
                    value = viewModel.newDatasetTitle,
                    placeholder = "Contoh: Data Penjualan Tahunan",
                    onValueChange = { viewModel.newDatasetTitle = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatasetTextField(
                    label = "2. Deskripsi Dataset",
                    value = viewModel.newDatasetDescription,
                    placeholder = "Deskripsikan isi dan tujuan dataset Anda",
                    onValueChange = { viewModel.newDatasetDescription = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatasetTextField(
                    label = "3. Kategori Dataset",
                    value = viewModel.newDatasetCategory,
                    placeholder = "Contoh: Keuangan, Kesehatan, Olahraga",
                    onValueChange = { viewModel.newDatasetCategory = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DatasetTextField(
                    label = "4. Format Dataset",
                    value = viewModel.newDatasetFormat,
                    placeholder = "Contoh: CSV, Excel, JSON",
                    onValueChange = { viewModel.newDatasetFormat = it }
                )
            }


            Spacer(modifier = Modifier.height(32.dp))

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
                    onClick = { navController.navigate("contributorDetail") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D124B))
                ) {
                    Text("Lanjutkan", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DatasetTextField(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Normal)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true
        )
    }
}