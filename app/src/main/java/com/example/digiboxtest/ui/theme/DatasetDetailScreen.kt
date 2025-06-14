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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    navController: NavController,
    datasetId: Int,
    viewModel: DatasetRoomViewModel
) {
    val dataset = viewModel.datasetList.find { it.id == datasetId }
    val bgGradient = Brush.verticalGradient(colors = listOf(Color(0xFFA1D4CA), Color.White))

    if (dataset == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Dataset not found.", color = Color.Red)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D124B), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DigiBox", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("About us", color = Color.White, fontSize = 12.sp)
                }
            }
        },
        containerColor = Color(0xFFA1D4CA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    color = Color(0xFFE0F2F1),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -- Header Section --
            Image(
                painter = if (dataset.profileImageUri != null) rememberAsyncImagePainter(dataset.profileImageUri) else painterResource(id = R.drawable.logo),
                contentDescription = dataset.title,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))
            Text(dataset.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(Modifier.height(16.dp))

            // -- Action Buttons --
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("Download", color = Color.White)
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Print", modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Print Details", color = Color.Black)
                }
            }
            Spacer(Modifier.height(24.dp))

            // -- Details Section --
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("About dataset:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))
                InfoRow(icon = Icons.Default.CarRental, label = "Kategori", value = dataset.category)
                InfoRow(icon = Icons.Default.BarChart, label = "Jumlah Data", value = "${dataset.rowCount} gambar")
                InfoRow(icon = Icons.Default.Search, label = "Fitur", value = dataset.description) // Using description as feature for now

                Spacer(Modifier.height(16.dp))
                DetailItem("Keywords:", dataset.keywords)
                DetailItem("Creator:", dataset.creator)
                DetailItem("Verifikator:", dataset.verifier)
                Spacer(Modifier.height(16.dp))
                Text("Activity Detail:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }

            // -- Charts (Placeholder) --
            // NOTE: For real charts, you need to add a charting library dependency
            // and feed it with actual data. This is a placeholder.
            ChartPlaceholder(title = "Views")
            ChartPlaceholder(title = "Downloads")
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
        Icon(icon, contentDescription = label, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(text = "$label: ", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(value, fontSize = 14.sp)
    }
}

@Composable
fun ChartPlaceholder(title: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, fontWeight = FontWeight.Medium)
            Text("Last month ▾", fontSize = 12.sp, color = Color.Gray)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 8.dp)
                .background(Color(0xFFCFD8DC), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Chart Area", color = Color.Gray)
        }
    }
}