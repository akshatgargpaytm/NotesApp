package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){
    val notes = repository.getAllNotes()
    fun addNote(note: NoteEntity) = viewModelScope.launch{
        repository.insertNote(note)
    }
    fun deleteNote(note: NoteEntity) = viewModelScope.launch{
        repository.deleteNote(note)
    }
    fun syncNotes() = viewModelScope.launch{
        repository.syncNotes()
    }
}