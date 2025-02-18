package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){
    private val _isAddingNote = MutableLiveData<Boolean>()
    val isAddingNote: LiveData<Boolean> get() = _isAddingNote
    private val _errorMessage = MutableLiveData<Boolean>()
    val errorMessage: LiveData<Boolean> get() = _errorMessage
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess
    fun addNote(title: String, content: String, imageUrl: String? = null){
        if(title.isEmpty() || content.isEmpty()) return
        viewModelScope.launch{
            _isAddingNote.value = true
            try {
                val newNote = NoteEntity(id = 0, title = title, content = content, imageUrl = imageUrl)
                repository.insertNote(newNote)
            }
            finally {
                _isAddingNote.value = false
            }
        }

    }
    fun clearError(){
        _errorMessage.value = null
    }
}