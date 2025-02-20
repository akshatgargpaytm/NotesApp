package com.example.notestakingapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.remote.api.ImageApiService
import com.example.notestakingapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val imageApiService: ImageApiService
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
                repository.insertNote(newNote)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add note: ${e.message}"
            } finally {
                _isAddingNote.value = false
            }
        }
    }

    // upload image
    fun uploadImage(file:File){
        viewModelScope.launch{
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body  = MultipartBody.Part.createFormData("image", file.name, requestFile)
            try {
                val response = imageApiService.uploadImage(body)
                if (response.isSuccessful){
                    _uploadStatus.value = response.body()?.imageUrl
                }
                else{
                    _uploadStatus.value = "Upload failed!"
                }
            }
            catch(e:Exception){
                _uploadStatus.value = "Error: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
