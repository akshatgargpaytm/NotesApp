package com.example.notestakingapp.data.remote.api

import com.example.notestakingapp.data.local.entity.NoteEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi{
    @GET("/notes")
    suspend fun getNotes(): List<NoteEntity>
    @POST("/notes")
    suspend fun createNote(@Body note: NoteEntity)
    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body note: NoteEntity)
    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int, note: NoteEntity)
}