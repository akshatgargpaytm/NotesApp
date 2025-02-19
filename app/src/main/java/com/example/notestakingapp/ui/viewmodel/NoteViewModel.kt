package com.example.notestakingapp.ui.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.data.repository.NoteRepository
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.example.notesapp.ui.viewmodel.NoteListViewModel
import com.example.notestakingapp.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {
    val allNotes = repository.getAllNotes().asLiveData()

    fun addNote(title: String, content:String) {
        val note = NoteEntity(title = title, content = content)
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}



//
//class NoteListFragment : Fragment(R.layout.fragment_note_list) {
//
//    private val viewModel: NoteListViewModel by viewModels()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Observe notes
//        viewModel.notes.observe(viewLifecycleOwner) { notes ->
//            // Update RecyclerView adapter here
//        }
//
//        // Observe loading state
//        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            if (isLoading) {
//                // Show loading spinner
//            } else {
//                // Hide loading spinner
//            }
//        }
//
//        // Trigger sync
//        viewModel.syncNotes()
//    }
//}
