package com.example.digiboxtest.network

import com.example.digiboxtest.ui.theme.DatasetRequest
import com.example.digiboxtest.ui.theme.DetailedDatasetRequest
import com.example.digiboxtest.ui.theme.ReplyRequestBody
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET("api-content/dataset-requests/")
    suspend fun getDatasetRequests(): Response<List<DatasetRequest>>

    @GET("api-content/dataset-requests/{id}/")
    suspend fun getDatasetRequestDetail(@Path("id") id: Int): Response<DetailedDatasetRequest>

    /**
     * Mengirim data balasan. Menggunakan @Url karena alamat API untuk reply
     * (undirty.pythonanywhere.com) berbeda dari base URL utama.
     *
     * @param fullUrl Alamat lengkap API untuk reply.
     * @param requestBody Data yang akan dikirim dalam format JSON.
     */
    @POST
    suspend fun sendReply(
        @Url fullUrl: String,
        @Body requestBody: ReplyRequestBody
    ): Response<JsonObject> // Response bisa disesuaikan, JsonObject untuk general
}