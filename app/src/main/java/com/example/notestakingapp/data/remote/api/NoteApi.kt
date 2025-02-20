package com.example.notestakingapp.data.remote.api

import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.remote.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface NoteApi {

    // Fetch all notes
    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteEntity>>

    // Create a new note
    @POST("/notes")
    suspend fun createNote(@Body note: NoteEntity): Response<NoteEntity>

    // Update an existing note by ID
    @PUT("/notes/{id}")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body note: NoteEntity
    ): Response<NoteEntity>

    // Delete a note by ID (No body for DELETE method)
    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int): Response<Unit>

    // Upload an image (Returns structured response)
    @Multipart
    @POST("/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>
}
