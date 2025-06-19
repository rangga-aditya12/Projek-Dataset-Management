package com.example.digiboxtest.ui.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.components.ImagePickerButton
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDatasetScreen(
    navController: NavController,
    viewModel: DatasetRoomViewModel,
    datasetId: Int
) {
    // State lokal untuk menampung perubahan sebelum disimpan
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var creator by remember { mutableStateOf("") }
    var verifier by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) } // NEW: State for profile image URI

    // Mengambil data dataset yang akan diedit saat layar pertama kali dibuka
    LaunchedEffect(key1 = datasetId) {
        val dataset = viewModel.datasetList.find { it.id == datasetId }
        if (dataset != null) {
            title = dataset.title
            description = dataset.description
            category = dataset.category
            creator = dataset.creator
            verifier = dataset.verifier
            profileImageUri = dataset.profileImageUri?.let { Uri.parse(it) } // NEW: Set initial image URI
        }
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFA1D4CA), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Dataset") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Ubah Informasi Dataset",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // NEW: Display current profile image and add ImagePickerButton
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Default Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.height(16.dp))

            // NEW: Image Picker Button
            Text("Edit Profil Dataset (Gambar)", fontSize = 14.sp)
            ImagePickerButton(onImagePicked = { uri ->
                profileImageUri = uri // Update the state with the new URI
            })

            Spacer(Modifier.height(16.dp))

            DatasetTextField(label = "Nama Dataset", value = title, onValueChange = { title = it })
            Spacer(Modifier.height(8.dp))
            DatasetTextField(label = "Deskripsi Dataset", value = description, onValueChange = { description = it })
            Spacer(Modifier.height(8.dp))
            DatasetTextField(label = "Kategori Dataset", value = category, onValueChange = { category = it })
            Spacer(Modifier.height(16.dp))
            ContributorTextField(label = "Nama Pembuat", value = creator, onValueChange = { creator = it })
            Spacer(Modifier.height(8.dp))
            ContributorTextField(label = "Nama Pemverifikasi", value = verifier, onValueChange = { verifier = it })

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val originalDataset = viewModel.datasetList.find { it.id == datasetId }
                    if (originalDataset != null) {
                        val updatedDataset = originalDataset.copy(
                            title = title,
                            description = description,
                            category = category,
                            keywords = category,
                            creator = creator,
                            verifier = verifier,
                            profileImageUri = profileImageUri?.toString() // NEW: Update profile image URI
                        )
                        viewModel.updateDataset(updatedDataset)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}