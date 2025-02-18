package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){
    val notes: LiveData<List<NoteEntity>> = repository.getAllNotes().asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess
    fun syncNotes(){
        viewModelScope.launch{
            _isLoading.value = true
            try{
                repository.syncNotes()
                _isSuccess.value = true
            }
            catch (e: Exception){
                _errorMessage.value = "Failed to sync notes: ${e.message}"
            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun clearError(){
        _errorMessage.value = null
    }
}