package com.example.notestakingapp.data.repository

import com.example.notestakingapp.data.local.dao.NoteDao
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.remote.api.NoteApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi
) : NoteRepository {
    override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()
    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
        try {
            noteApi.createNote(note)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
        try {
            noteApi.updateNote(note.id, note)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
        try {
            noteApi.deleteNote(note.id, note)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun syncNotes() {
        try {
            val remoteNotes = noteApi.getNotes()
            noteDao.insertNotes(remoteNotes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getNoteById(noteId: Int): NoteEntity? {
        TODO("Not yet implemented")
    }


}