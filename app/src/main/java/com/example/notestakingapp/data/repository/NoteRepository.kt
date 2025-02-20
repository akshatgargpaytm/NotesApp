package com.example.notestakingapp.data.repository

import com.example.notestakingapp.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    suspend fun insertNote(note: NoteEntity)
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
    suspend fun getNoteById(noteId: Int): NoteEntity?
    suspend fun saveImageUrl(noteId: Int, imageUrl: String)
    suspend fun syncNotes()
}
