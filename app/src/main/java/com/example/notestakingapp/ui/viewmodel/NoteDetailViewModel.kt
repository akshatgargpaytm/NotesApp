package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    // Note object to display
    private val _note = MutableLiveData<NoteEntity>()
    val note: LiveData<NoteEntity> get() = _note

    // Load a note
    fun loadNote(note: NoteEntity) {
        _note.value = note
    }

    // Update a note
    fun updateNote(updatedNote: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(updatedNote)
            _note.value = updatedNote
        }
    }

    // Delete a note
    fun deleteNote() {
        viewModelScope.launch {
            note.value?.let { repository.deleteNote(it) }
        }
    }
}
