package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _note = MutableLiveData<NoteEntity?>()
    val note: LiveData<NoteEntity?> get() = _note

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // ✅ Load Note from Database
    fun loadNote(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val loadedNote = repository.getNoteById(noteId)
                _note.postValue(loadedNote)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // ✅ Update Note on Background Thread
    fun updateNote(updatedNote: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                repository.updateNote(updatedNote)
                _note.postValue(updatedNote) // Update UI after saving
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // ✅ Delete Note on Background Thread
    fun deleteNote(noteId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val noteToDelete = _note.value
                if (noteToDelete != null) {
                    repository.deleteNote(noteToDelete)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
