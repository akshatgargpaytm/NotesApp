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

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>) // ✅ Atomic batch insert

    @Query("UPDATE notes SET imageUrl = :imageUrl WHERE id = :noteId")
    suspend fun updateImageUrl(noteId: Int, imageUrl: String)

    @Query("UPDATE notes SET title = :title, content = :content WHERE id = :id")
    suspend fun updateNoteFields(id: Int, title: String, content: String) // ✅ Partial update

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int) // ✅ More efficient than @Delete

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity
    abstract fun updateNote(note: NoteEntity)
    abstract fun deleteNote(note: NoteEntity)
}
