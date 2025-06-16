package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.digiboxtest.R
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@Composable
fun ContributorDetailScreen(navController: NavController, viewModel: DatasetRoomViewModel) {
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
                "Informasi Lanjutan Dataset",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D124B)
            )
            Text(
                "Isi informasi tambahan yang dibutuhkan untuk mendeskripsikan dataset Anda secara menyeluruh.",
                fontSize = 13.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                ContributorTextField(
                    label = "Nama Pembuat Dataset",
                    value = viewModel.newDatasetCreator,
                    onValueChange = { viewModel.newDatasetCreator = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContributorTextField(
                    label = "Nama Pemverifikasi",
                    value = viewModel.newDatasetVerifier,
                    onValueChange = { viewModel.newDatasetVerifier = it }
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
                    onClick = { navController.navigate("datasetMetadata") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D124B))
                ) {
                    Text("Lanjutkan", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ContributorTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Normal)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            singleLine = true
        )
    }
}