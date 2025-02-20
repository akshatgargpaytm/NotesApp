package com.example.notestakingapp.data.remote.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Response
import okhttp3.ResponseBody



interface ImageApiService {
    @Multipart
    @POST("/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>
}
