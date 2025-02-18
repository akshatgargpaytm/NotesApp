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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: List<NoteEntity>)
    @Update
    suspend fun updateNote(note: NoteEntity)
    @Delete
    suspend fun deleteNote(note: NoteEntity)

}