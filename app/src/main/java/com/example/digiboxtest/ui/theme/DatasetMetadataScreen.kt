package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digiboxtest.R

@Composable
fun DatasetMetadataScreen(navController: NavController) {
    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFA1D4CA), Color.White)
    )

    var rowCount by remember { mutableStateOf("") }
    var featureCount by remember { mutableStateOf("") }
    var keyword by remember { mutableStateOf("") }
    var fileSelected by remember { mutableStateOf("Pilih File") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DigiBox", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                }

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Mulai Proyek Anda dengan Dataset Baru",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0D124B)
            )
            Text(
                "Buat dataset baru untuk dianalisis, dibagikan, atau digunakan dalam model Anda dengan mudah dan efisien sesuai dengan kebutuhan anda.",
                fontSize = 13.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Informasi Dataset", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Text("1. File Dataset")
            Button(
                onClick = { /* file picker logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD))
            ) {
                Text(fileSelected, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
            DatasetTextField("2. Jumlah Baris (Row)", rowCount) { rowCount = it }
            DatasetTextField("3. Jumlah Fitur", featureCount) { featureCount = it }
            DatasetTextField("4. Kata Kunci Pencarian Dataset anda", keyword) { keyword = it }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2E34))
                ) {
                    Text("Back", color = Color.White)
                }
                Button(
                    onClick = { /* Submit logic here */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2E34))
                ) {
                    Text("Submit", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D124B), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DigiBox", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("About us", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}
