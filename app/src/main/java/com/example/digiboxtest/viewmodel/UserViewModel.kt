package com.example.digiboxtest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.database.AppDatabase
import com.example.digiboxtest.database.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun signUp(user: UserEntity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.findByUsername(user.username)
            if (existingUser == null) {
                userDao.insertUser(user)
                onSuccess()
            } else {
                onError("Username already exists.")
            }
        }
    }

    fun login(username: String, passwordHash: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = userDao.findByUsername(username)
            if (user != null && user.passwordHash == passwordHash) {
                onSuccess()
            } else {
                onError("Invalid username or password.")
            }
        }
    }
}