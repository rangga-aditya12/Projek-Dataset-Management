package com.example.digiboxtest.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.digiboxtest.R
import com.example.digiboxtest.viewmodel.DatasetRequestsViewModel
import com.google.gson.annotations.SerializedName

// --- DATA CLASS YANG SUDAH DIPERBAIKI TOTAL ---

/**
 * Data class utama yang merepresentasikan satu item dalam daftar JSON.
 */
data class DatasetRequest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("project_detail")
    val projectDetail: ProjectDetail?, // Objek di dalam JSON

    @SerializedName("requested_by_detail")
    val requestedBy: RequestedBy?, // Objek di dalam JSON

    @SerializedName("created_at")
    val date: String?
)

/**
 * Data class untuk objek "project_detail" di dalam JSON (untuk daftar).
 */
data class ProjectDetail(
    @SerializedName("name")
    val title: String?
)

/**
 * Data class untuk objek "requested_by_detail" di dalam JSON.
 */
data class RequestedBy(
    @SerializedName("username")
    val sender: String?
)

/**
 * Data class untuk halaman detail, sekarang strukturnya sama persis dengan JSON.
 */
data class DetailedDatasetRequest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("project_detail")
    val projectDetail: DetailedProjectInfo?, // Menggunakan data class baru untuk detail

    @SerializedName("description_problem")
    val problemDescription: String?,

    @SerializedName("target_for_dataset")
    val target: String?,

    @SerializedName("type_data_needed")
    val dataType: String?,

    @SerializedName("data_processing_activity")
    val processingActivity: String?,

    @SerializedName("num_features")
    val featureCount: Int?,

    @SerializedName("dataset_size")
    val datasetSize: String?,

    @SerializedName("file_format")
    val fileFormat: String?,

    @SerializedName("start_date_needed")
    val startDate: String?,

    @SerializedName("end_date_needed")
    val endDate: String?,

    @SerializedName("status")
    val status: String?
)

/**
 * Data class baru untuk menampung objek "project_detail" yang lebih lengkap di halaman detail.
 */
data class DetailedProjectInfo(
    @SerializedName("name")
    val name: String?
)


@Composable
fun DatasetRequestsScreen(
    navController: NavController,
    viewModel: DatasetRequestsViewModel = viewModel()
) {
    val requests by viewModel.requests.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val backgroundColor = Color(0xFFE4F5F8)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "DigiBox Logo",
                    modifier = Modifier.size(50.dp)
                )
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D124B))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("DigiBox", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("About us", color = Color.White, fontSize = 12.sp)
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dataset Requests", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.fetchLatestRequests() },
                    enabled = !isLoading,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ambil Pesan Terbaru", fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2FB5C4), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(text = message, color = Color.White, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(requests) { request ->
                        RequestCard(
                            request = request,
                            onViewDetailsClick = {
                                navController.navigate("datasetRequestDetail/${request.id}")
                            },
                            onReplyClick = {
                                val title = request.projectDetail?.title ?: "Unknown"
                                navController.navigate("reply/${request.id}/$title")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: DatasetRequest,
    onReplyClick: () -> Unit,
    onViewDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.projectDetail?.title ?: "Tanpa Judul",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dari: ${request.requestedBy?.sender ?: "-"} | Diterima: ${request.date ?: "-"}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onViewDetailsClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("View Details", fontSize = 12.sp, color = Color.White)
                }
                Button(
                    onClick = onReplyClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Reply", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}