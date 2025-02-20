package com.example.notestakingapp.data.repository

import com.example.notestakingapp.data.local.dao.NoteDao
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.remote.api.NoteApi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi
) : NoteRepository {

    override suspend fun saveImageUrl(noteId: Int, imageUrl: String) {
        noteDao.updateImageUrl(noteId, imageUrl)
    }

    suspend fun uploadImage(image: MultipartBody.Part) = noteApi.uploadImage(image)

    override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note) // Save to local database
        try {
            noteApi.createNote(note) // Sync to remote API
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note) // Update local database
        try {
            noteApi.updateNote(note.id, note) // Sync to remote API
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note) // Delete from local database
        try {
            noteApi.deleteNote(note.id) // Sync deletion to remote API
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun syncNotes() {
        try {
            val response = noteApi.getNotes() // Fetch from remote API

            if (response.isSuccessful) {
                response.body()?.let { remoteNotes ->
                    noteDao.insertNotes(remoteNotes) // ✅ Insert only the list
                }
            } else {
                println("Error syncing notes: ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getNoteById(noteId: Int): NoteEntity? {
        return noteDao.getNoteById(noteId) // Fetch note by ID
    }
}
