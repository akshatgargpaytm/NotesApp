package com.example.notestakingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    init {
        Log.d("DI_SUCCESS", "NoteRepository injected successfully!")
    }

    // LiveData for notes from the Room database via Flow
    val notes: LiveData<List<NoteEntity>> = repository.getAllNotes().asLiveData()

    // State: Loading, Error, Success
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    // Sync notes from remote (if applicable)
    fun syncNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.syncNotes()
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to sync notes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Clear error message after displaying it
    fun clearError() {
        _errorMessage.value = null
    }
}
