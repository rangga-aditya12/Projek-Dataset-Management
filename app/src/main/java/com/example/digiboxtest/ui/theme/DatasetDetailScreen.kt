package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@Composable
fun DatasetDetailScreen(
    navController: NavController,
    datasetId: Int,
    viewModel: DatasetRoomViewModel
) {
    val dataset = viewModel.datasetList.find { it.id == datasetId }

    if (dataset == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Dataset not found.", color = Color.Red)
        }
    } else {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Dataset Detail", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Title: ${dataset.title}", fontSize = 18.sp)
            Text("Description: ${dataset.description}", fontSize = 16.sp)
            Text("Rows: ${dataset.rowCount}", fontSize = 16.sp)
            Text("Features: ${dataset.featureCount}", fontSize = 16.sp)
            Text("Keywords: ${dataset.keywords}", fontSize = 16.sp)
            Text("Last Update: ${dataset.lastUpdate}", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Back", color = Color.White)
            }
        }
    }
}
