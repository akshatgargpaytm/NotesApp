package com.example.notestakingapp.data.local.dao

import androidx.room.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    // Accepts List<NoteEntity> instead of Response<List<NoteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)

    @Query("UPDATE notes SET imageUrl = :imageUrl WHERE id = :noteId")
    suspend fun updateImageUrl(noteId: Int, imageUrl: String)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity
}
