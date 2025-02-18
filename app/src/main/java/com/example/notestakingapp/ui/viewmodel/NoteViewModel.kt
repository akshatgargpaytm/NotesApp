package com.example.notestakingapp.ui.viewmodel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notestakingapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val viewModel: NoteListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe notes
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            // Update RecyclerView adapter here
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Show loading spinner
            } else {
                // Hide loading spinner
            }
        }

        // Trigger sync
        viewModel.syncNotes()
    }
}
