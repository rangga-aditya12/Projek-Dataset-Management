package com.example.digiboxtest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserEntity?

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    fun findByUsernameFlow(username: String): Flow<UserEntity?> // <-- TAMBAHKAN FUNGSI INI

    @Update
    suspend fun updateUser(user: UserEntity) // <-- TAMBAHKAN FUNGSI INI
}