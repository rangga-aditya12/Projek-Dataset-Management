package com.example.digiboxtest.ui.theme

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.digiboxtest.viewmodel.UserViewModel

// UPDATED: onLoginSuccess now passes the username (String)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (String) -> Unit, context: Context) {
    val bgGradient = Brush.verticalGradient(listOf(Color(0xFFA1D4CA), Color.White))
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // NEW: Initialize UserViewModel and get context
    val userViewModel: UserViewModel = viewModel()
    val localContext = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color(0xFFDCE6E5), shape = RoundedCornerShape(30.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Login", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Username") },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Forgot Password?", fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // UPDATED: onClick logic now uses UserViewModel for authentication
                Button(
                    onClick = {
                        if (username.isNotBlank() && password.isNotBlank()) {
                            // In a real app, hash the password before sending
                            userViewModel.login(
                                username = username,
                                passwordHash = password,
                                onSuccess = {
                                    // Pass the username to the callback on success
                                    onLoginSuccess(username)
                                },
                                onError = { errorMsg ->
                                    Toast.makeText(localContext, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            Toast.makeText(localContext, "Please enter username and password.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D2E34))
                ) {
                    Text("Login", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Create account",
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    }
                )
            }
        }
    }
}

