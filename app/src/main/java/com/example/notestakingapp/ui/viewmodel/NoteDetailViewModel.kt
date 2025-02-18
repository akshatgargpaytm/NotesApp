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
    private val _note = MutableLiveData<NoteEntity?>()
    val note: MutableLiveData<NoteEntity?> get() = _note

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Set loading state while performing operations
    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val note = repository.getNoteById(noteId)
                _note.postValue(note)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNote(updatedNote: NoteEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateNote(updatedNote)
            _note.postValue(updatedNote)
            _isLoading.value = false
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            _isLoading.value = true
            note.value?.let { repository.deleteNote(it) }
            _isLoading.value = false
        }
    }
}
