package com.example.digiboxtest.ui.theme

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digiboxtest.R
import com.example.digiboxtest.database.DatasetEntity
import com.example.digiboxtest.viewmodel.DatasetRoomViewModel

@Composable
fun DatasetListScreen(navController: NavController, viewModel: DatasetRoomViewModel) {
    val datasetList = viewModel.datasetList

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Your Datasets", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (datasetList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No datasets found.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(datasetList.size) { index ->
                    val dataset = datasetList[index]
                    DatasetCard(
                        dataset = dataset,
                        onViewClick = {
                            // TODO: Implement navigation to detail screen
                            navController.navigate("datasetDetail/${dataset.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteDataset(dataset)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DatasetCard(
    dataset: DatasetEntity,
    onViewClick: () -> Unit,
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
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Updated: ${dataset.lastUpdate}", fontSize = 12.sp, color = Color.Gray)
                Row {
                    Button(
                        onClick = onViewClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
                    ) {
                        Text("View", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    }
}
