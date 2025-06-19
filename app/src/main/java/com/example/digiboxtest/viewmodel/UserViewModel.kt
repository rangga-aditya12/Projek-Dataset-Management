package com.example.digiboxtest.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.digiboxtest.database.AppDatabase
import com.example.digiboxtest.database.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    /**
     * Mendaftarkan pengguna baru.
     *
     */
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

    /**
     * Melakukan proses login untuk pengguna.
     *
     */
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

    /**
     * Memperbarui URI gambar profil untuk pengguna tertentu.
     */
    fun updateUserProfileImage(username: String, imageUri: Uri) {
        viewModelScope.launch {
            val user = userDao.findByUsername(username)
            user?.let {
                // Membuat salinan user dengan URI gambar yang baru
                val updatedUser = it.copy(profileImageUri = imageUri.toString())
                // Memanggil fungsi update di DAO
                userDao.updateUser(updatedUser)
            }
        }
    }

    /**
     * Mendapatkan data pengguna secara real-time menggunakan Flow.
     */
    fun getUser(username: String) = userDao.findByUsernameFlow(username)
}