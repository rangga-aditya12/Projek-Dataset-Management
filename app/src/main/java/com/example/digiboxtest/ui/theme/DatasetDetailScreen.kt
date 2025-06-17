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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.utils.downloadCsvFile
import com.example.digiboxtest.utils.printDatasetDetails
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    navController: NavController,
    datasetId: Int,
    viewModel: DatasetRoomViewModel
) {
    // Menemukan dataset berdasarkan ID, dan mengambil LocalContext untuk digunakan di fungsi utilitas.
    val dataset = viewModel.datasetList.find { it.id == datasetId }
    val context = LocalContext.current
    val bgGradient = Brush.verticalGradient(colors = listOf(Color(0xFFA1D4CA), Color.White))

    // Tampilkan pesan jika dataset tidak ditemukan.
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
            // Footer kustom
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
        containerColor = Color(0xFFA1D4CA) // Warna latar belakang utama Scaffold
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    color = Color.White, // Latar belakang putih untuk area konten utama
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -- Header Section --
            Image(
                painter = if (dataset.profileImageUri != null) rememberAsyncImagePainter(dataset.profileImageUri) else painterResource(id = R.drawable.logo),
                contentDescription = dataset.title,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))
            Text(dataset.title, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF0D124B))
            Text(text = "Updated: ${dataset.lastUpdate}", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(24.dp))

            // -- Tombol Aksi (Download & Print) --
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        downloadCsvFile(
                            context = context,
                            fileUriString = dataset.fileUri,
                            title = dataset.title
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)) // Biru Tua
                ) {
                    Text("Download", color = Color.White)
                }
                Button(
                    onClick = {
                        printDatasetDetails(context, dataset)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCFD8DC)), // Abu-abu muda
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Print", modifier = Modifier.size(20.dp), tint = Color.Black)
                    Spacer(Modifier.width(4.dp))
                    Text("Print Details", color = Color.Black)
                }
            }
            Spacer(Modifier.height(32.dp))

            // -- Bagian Detail --
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("About this Dataset:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.Black)
                Spacer(Modifier.height(12.dp))

                // Menggunakan DetailItem untuk konsistensi
                DetailItem(icon = Icons.Default.Info, label = "Description", value = dataset.description)
                DetailItem(icon = Icons.Default.ListAlt, label = "Category", value = dataset.category)
                DetailItem(icon = Icons.Default.BarChart, label = "Data Size", value = "${dataset.rowCount} rows • ${dataset.featureCount} features")
                DetailItem(icon = Icons.Default.Person, label = "Creator", value = dataset.creator)
                DetailItem(icon = Icons.Default.VerifiedUser, label = "Verifier", value = dataset.verifier)
                DetailItem(icon = null, label = "Keywords", value = dataset.keywords)

                Spacer(Modifier.height(24.dp))
                Text("Activity Detail:", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }

            // -- Placeholder untuk Grafik --
            ChartPlaceholder(title = "Views")
            ChartPlaceholder(title = "Downloads")
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector?, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.DarkGray,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 4.dp)
            )
            Spacer(Modifier.width(16.dp))
        }
        Column {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}


@Composable
fun ChartPlaceholder(title: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text("Last month ▾", fontSize = 12.sp, color = Color.Gray)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 8.dp)
                .background(Color(0xFFECEFF1), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Chart Area", color = Color.Gray)
        }
    }
}