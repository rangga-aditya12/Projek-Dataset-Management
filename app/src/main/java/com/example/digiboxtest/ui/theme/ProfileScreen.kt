package com.example.digiboxtest.ui.theme

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.digiboxtest.R
import com.example.digiboxtest.components.ImagePickerButton
import com.example.digiboxtest.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val username by userPreferences.loggedInUsername.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel()

    // Mengambil data user secara real-time dari database
    val user by if (username != null) {
        userViewModel.getUser(username!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Update URI di state saat data user berubah
    LaunchedEffect(user) {
        user?.profileImageUri?.let {
            profileImageUri = Uri.parse(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Tampilkan gambar profil
        Image(
            painter = if (profileImageUri != null) {
                rememberAsyncImagePainter(profileImageUri)
            } else {
                painterResource(id = R.drawable.ic_launcher_foreground) // Gambar default
            },
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol untuk mengubah gambar
        ImagePickerButton { newUri ->
            newUri?.let {
                profileImageUri = it
                username?.let { currentUsername ->
                    userViewModel.updateUserProfileImage(currentUsername, it)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome, ${username ?: "User"}!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("datasetList") }) {
            Text("View My Datasets")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    onLogout()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout")
        }
    }
}