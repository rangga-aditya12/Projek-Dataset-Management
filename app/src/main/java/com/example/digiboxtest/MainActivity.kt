package com.example.digiboxtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digiboxtest.ui.theme.ContributorDetailScreen
import com.example.digiboxtest.ui.theme.CreateDatasetScreen
import com.example.digiboxtest.ui.theme.DatasetCollectionScreen
import com.example.digiboxtest.ui.theme.DatasetMetadataScreen
import com.example.digiboxtest.ui.theme.LoginScreen
import com.example.digiboxtest.ui.theme.SignUpScreen
import com.example.digiboxtest.ui.theme.UserPreferences
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigiBoxApp()
        }
    }
}

@Composable
fun DigiBoxApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    // collect login status dari datastore
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // delay kecil untuk simulasi loading
        kotlinx.coroutines.delay(500)
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(navController = navController, startDestination = if (isLoggedIn) "home" else "login") {
            composable("home") {
                DigiboxMobileUI(navController, isLoggedIn)
            }
            composable("dataset") {
                DatasetCollectionScreen()
            }
            composable("login") {
                val coroutineScope = rememberCoroutineScope()

                LoginScreen(
                    navController = navController,
                    onLoginSuccess = {
                        coroutineScope.launch {
                            userPreferences.saveLoginStatus(true)
                        }
                    },
                    context = context
                )
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("createDataset") {
                if (isLoggedIn) {
                    CreateDatasetScreen(navController)
                } else {
                    navController.navigate("login")
                }
            }
            composable("contributorDetail") { ContributorDetailScreen(navController) }
            composable("datasetMetadata") { DatasetMetadataScreen(navController, viewModel()) }
            }
    }
}



@Composable
fun DigiboxMobileUI(navController: NavController, isLoggedIn: Boolean) {
    val bgGradient = Brush.verticalGradient(
        listOf(Color(0xFFA1D4CA), Color.White)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.navigate("login") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Jelajahi Dunia Data, Tanpa Batas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "Temukan dan akses beragam dataset berkualitas tinggi untuk mendukung riset, analisis, dan pengembangan teknologi. Dari data sains hingga kecerdasan buatan, semua tersedia dalam satu platform yang mudah digunakan.",
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (isLoggedIn) {
                    navController.navigate("createDataset")
                } else {
                    navController.navigate("login")
                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2F1)),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Create Dataset", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionRow("All Dataset") { navController.navigate("dataset") }
        Spacer(modifier = Modifier.height(8.dp))
        DatasetRow()

        Spacer(modifier = Modifier.height(24.dp))
        SectionRow("Last View") { }
        Spacer(modifier = Modifier.height(8.dp))
        DatasetRow()

        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D124B), shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DigiBox", color = Color.White, fontWeight = FontWeight.Bold)
                Text("About us", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SectionRow(title: String, onViewAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(
            "View all →",
            fontSize = 13.sp,
            color = Color.Black,
            modifier = Modifier.clickable { onViewAllClick() }
        )
    }
}

@Composable
fun DatasetRow() {
    LazyRow {
        items(4) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(120.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(20.dp))
            )
        }
    }
}
