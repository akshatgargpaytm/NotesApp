package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel  @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){
    private val _note = MutableLiveData<NoteEntity>()
    val note: LiveData<NoteEntity> get() = _note

    fun loadNote(note: NoteEntity){
        _note.value = note
    }
    fun updateNote(updateNote: NoteEntity){
        viewModelScope.launch {
            note.value?.let { repository.deleteNote(it)}
        }

    }
}