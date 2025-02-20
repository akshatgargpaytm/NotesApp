package com.example.notestakingapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> = _uploadStatus

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
                val noteId = repository.insertNote(newNote) // Get the noteId from DB

                _isSuccess.value = true
                Log.d("AddNote", "Note added successfully with ID: $noteId")
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add note: ${e.message}"
                Log.e("AddNote", "Error adding note: ${e.message}")
            } finally {
                _isAddingNote.value = false
            }
        }
    }

    // Upload image and save its URL
    fun uploadImage(file: File, noteId: Int) {
        viewModelScope.launch {
            try {
                // Create Multipart for the image
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                // Simulate upload (or replace with actual upload logic)
                val imageUrl = "file://${file.absolutePath}"
                repository.saveImageUrl(noteId, imageUrl)

                _uploadStatus.postValue("Image uploaded successfully!")
                Log.d("Upload", "Image uploaded: $imageUrl")

            } catch (e: Exception) {
                _uploadStatus.postValue("Error: ${e.message}")
                Log.e("Upload", "Image upload failed: ${e.message}")
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
