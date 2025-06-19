package com.example.digiboxtest.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val username: String,
    val passwordHash: String, // Di aplikasi nyata, selalu simpan hash password, bukan plain text
    val profileImageUri: String? = null // <-- TAMBAHKAN BARIS INI
)