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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Inbox
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.digiboxtest.ui.theme.ContributorDetailScreen
import com.example.digiboxtest.ui.theme.CreateDatasetScreen
import com.example.digiboxtest.ui.theme.DatasetDetailScreen
import com.example.digiboxtest.ui.theme.DatasetListScreen
import com.example.digiboxtest.ui.theme.DatasetMetadataScreen
import com.example.digiboxtest.ui.theme.DatasetRequestsScreen
import com.example.digiboxtest.ui.theme.EditDatasetScreen
import com.example.digiboxtest.ui.theme.LoginScreen
import com.example.digiboxtest.ui.theme.ProfileScreen
import com.example.digiboxtest.ui.theme.SignUpScreen
import com.example.digiboxtest.ui.theme.UserPreferences
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel
import com.example.digiboxtest.viewmodel.UserViewModel
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
    val datasetViewModel: DatasetRoomViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
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
                DigiboxMobileUI(navController, isLoggedIn) {
                    if (isLoggedIn) {
                        navController.navigate("profile")
                    } else {
                        navController.navigate("login")
                    }
                }
            }
            composable(
                route = "datasetList?query={query}",
                arguments = listOf(navArgument("query") { defaultValue = "" })
            ) { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                DatasetListScreen(
                    navController = navController,
                    viewModel = datasetViewModel,
                    initialQuery = query
                )
            }
            composable("login") {
                val coroutineScope = rememberCoroutineScope()
                LoginScreen(
                    navController = navController,
                    onLoginSuccess = { username ->
                        coroutineScope.launch {
                            userPreferences.saveLoginSession(true, username)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    context = context
                )
            }
            composable("signup") {
                SignUpScreen(navController)
            }
            composable("profile") {
                val coroutineScope = rememberCoroutineScope()
                ProfileScreen(navController = navController) {
                    coroutineScope.launch {
                        userPreferences.clearLoginSession()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            }
            composable("createDataset") {
                CreateDatasetScreen(navController, datasetViewModel)
            }
            composable("contributorDetail") {
                ContributorDetailScreen(navController, datasetViewModel)
            }
            composable("datasetMetadata") {
                DatasetMetadataScreen(navController, datasetViewModel)
            }
            composable(
                route = "datasetDetail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val datasetId = backStackEntry.arguments?.getInt("id")
                if (datasetId != null) {
                    DatasetDetailScreen(navController, datasetId, datasetViewModel)
                }
            }
            composable(
                route = "editDataset/{datasetId}",
                arguments = listOf(navArgument("datasetId") { type = NavType.IntType })
            ) { backStackEntry ->
                val datasetId = backStackEntry.arguments?.getInt("datasetId")
                if (datasetId != null) {
                    EditDatasetScreen(
                        navController = navController,
                        viewModel = datasetViewModel,
                        datasetId = datasetId
                    )
                }
            }
            // New route for Dataset Requests
            composable("datasetRequests") {
                DatasetRequestsScreen(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DigiboxMobileUI(navController: NavController, isLoggedIn: Boolean, onProfileClick: () -> Unit) {
    var localSearchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val bgGradient = Brush.verticalGradient(
        listOf(Color(0xFFA1D4CA), Color.White)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(16.dp)
    ) {
        // Top section with header and search bar
        Column(
            modifier = Modifier.weight(1f) // Gives this column weight to push the footer down
        ) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = "Dataset Requests",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.navigate("datasetRequests") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onProfileClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = localSearchQuery,
                onValueChange = { localSearchQuery = it },
                placeholder = { Text("Search datasets...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        navController.navigate("datasetList?query=${localSearchQuery}")
                    }
                )
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
            SectionRow("All Dataset") { navController.navigate("datasetList?query=") }
            Spacer(modifier = Modifier.height(8.dp))
            DatasetRow()

            Spacer(modifier = Modifier.height(24.dp))
            SectionRow("Last View") { }
            Spacer(modifier = Modifier.height(8.dp))
            DatasetRow()

            // A spacer with weight to push the content up and footer down
            Spacer(modifier = Modifier.weight(1f))
        }

        // Footer section
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