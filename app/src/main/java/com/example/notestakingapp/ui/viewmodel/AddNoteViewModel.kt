package com.example.notesapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _isAddingNote = MutableLiveData<Boolean>()
    val isAddingNote: LiveData<Boolean> get() = _isAddingNote

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    // Add a new note
    fun addNote(title: String, content: String, imageUrl: String? = null) {
        if (title.isEmpty() || content.isEmpty()) {
            _errorMessage.value = "Title and content cannot be empty."
            return
        }

        viewModelScope.launch {
            _isAddingNote.value = true
            try {
                val newNote = NoteEntity(0, title, content, imageUrl)
                repository.insertNote(newNote)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add note: ${e.message}"
            } finally {
                _isAddingNote.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
