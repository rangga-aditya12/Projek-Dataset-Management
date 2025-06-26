package com.example.digiboxtest.network

import com.example.digiboxtest.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    /**
     * [KODE BARU UNTUK DEBUGGING]
     * Kita buat 'HttpLoggingInterceptor' untuk mencetak semua detail request dan response
     * ke Logcat. Ini sangat membantu untuk melihat apakah URL dan data sudah benar.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 'BODY' akan menampilkan semua info: URL, headers, body
    }

    /**
     * [KODE BARU UNTUK DEBUGGING]
     * Kita buat OkHttpClient khusus yang menyertakan logging interceptor kita.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Instance Retrofit sekarang menggunakan client yang sudah ada logger-nya.
     */
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.DATASET_REQUEST_API_URL) // Base URL tetap
            .client(client) // <-- Gunakan client baru yang ada logger-nya
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}