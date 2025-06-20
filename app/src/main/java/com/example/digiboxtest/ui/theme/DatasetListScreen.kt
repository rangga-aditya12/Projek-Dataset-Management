package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.database.DatasetEntity
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@Composable
fun DatasetListScreen(
    navController: NavController,
    viewModel: DatasetRoomViewModel,
    initialQuery: String = ""
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }

    // --- PERUBAHAN 1: Tambahkan state untuk dialog ---
    var showDialog by remember { mutableStateOf(false) }
    var datasetToDelete by remember { mutableStateOf<DatasetEntity?>(null) }


    val filteredDatasetList = remember(searchQuery, viewModel.datasetList) {
        if (searchQuery.isBlank()) {
            viewModel.datasetList
        } else {
            viewModel.datasetList.filter { dataset ->
                dataset.title.contains(searchQuery, ignoreCase = true) ||
                        dataset.description.contains(searchQuery, ignoreCase = true) ||
                        dataset.keywords.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Your Datasets", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by title, description, or keyword...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredDatasetList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (searchQuery.isNotBlank()) "No datasets found for \"$searchQuery\"."
                    else "No datasets found.",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredDatasetList.size) { index ->
                    val dataset = filteredDatasetList[index]
                    DatasetCard(
                        dataset = dataset,
                        onViewClick = {
                            navController.navigate("datasetDetail/${dataset.id}")
                        },
                        onEditClick = {
                            navController.navigate("editDataset/${dataset.id}")
                        },
                        // --- PERUBAHAN 2: Ubah aksi onClick ---
                        onDeleteClick = {
                            // Simpan data yang akan dihapus dan tampilkan dialog
                            datasetToDelete = dataset
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    // --- PERUBAHAN 3: Tambahkan AlertDialog di sini ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Tutup dialog jika pengguna mengklik di luar area dialog
                showDialog = false
            },
            title = {
                Text(text = "Konfirmasi Hapus")
            },
            text = {
                Text(text = "Apakah Anda yakin ingin menghapus dataset \"${datasetToDelete?.title}\"? Tindakan ini tidak dapat dibatalkan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Jika dikonfirmasi, panggil fungsi hapus dari ViewModel
                        datasetToDelete?.let {
                            viewModel.deleteDataset(it)
                        }
                        showDialog = false
                    },
                    // Beri warna merah untuk menandakan aksi berbahaya
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Tutup dialog jika dibatalkan
                        showDialog = false
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}


@Composable
fun DatasetCard(
    dataset: DatasetEntity,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = if (dataset.profileImageUri != null) {
                        rememberAsyncImagePainter(dataset.profileImageUri)
                    } else {
                        painterResource(id = R.drawable.logo)
                    },
                    contentDescription = dataset.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(dataset.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("${dataset.rowCount} rows • ${dataset.featureCount} features")
                    Text("Tags: ${dataset.keywords}", fontSize = 12.sp, color = Color.DarkGray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(dataset.description, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Updated: ${dataset.lastUpdate}", fontSize = 12.sp, color = Color.Gray)
                Row {
                    val buttonPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

                    Button(
                        onClick = onViewClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                        contentPadding = buttonPadding
                    ) {
                        Text("View", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = onEditClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                        contentPadding = buttonPadding
                    ) {
                        Text("Edit", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                        contentPadding = buttonPadding
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
}