package com.example.digiboxtest.ui.theme

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.digiboxtest.viewmodel.DatasetRequestsViewModel
import kotlinx.coroutines.launch

/**
 * Data class untuk mendefinisikan body dari request API balasan.
 */
data class ReplyRequestBody(
    val replyMessage: String,
    val downloadLink: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyScreen(
    navController: NavController,
    projectTitle: String
) {
    var downloadLink by remember { mutableStateOf("") }
    var replyMessage by remember { mutableStateOf("hallo hallo download nih yakk") }
    val statusOptions = listOf("Complete", "In Progress", "Rejected", "Pending")
    var selectedStatus by remember { mutableStateOf(statusOptions[0]) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: DatasetRequestsViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4F5F8))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Balas Pesan untuk Proyek: \"$projectTitle\"",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kirimkan balasan, link download dataset, dan perbarui status permintaan.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = downloadLink,
                    onValueChange = { downloadLink = it },
                    label = { Text("Link Download Dataset") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        label = { Text("Ubah Status Permintaan") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = replyMessage,
                    onValueChange = { replyMessage = it },
                    label = { Text("Pesan Balasan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Kembali", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val replyApiUrl = "https://undirty.pythonanywhere.com/api/dataset-reply/"
                                val success = viewModel.sendReply(
                                    replyUrl = replyApiUrl,
                                    message = replyMessage,
                                    downloadLink = downloadLink,
                                    status = selectedStatus
                                )
                                if (success) {
                                    Toast.makeText(context, "Balasan berhasil terkirim!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Balasan berhasil terkirim!", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2FB5C4))
                    ) {
                        Text("Simpan dan Kirim Balasan")
                    }
                }
            }
        }
    }
}