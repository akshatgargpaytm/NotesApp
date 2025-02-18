package com.example.notesapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.notesapp.ui.viewmodel.NoteListViewModel
import com.example.notestakingapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val viewModel: NoteListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe notes and update RecyclerView (Assuming you have a RecyclerView)
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            // Update RecyclerView adapter here
            // adapter.submitList(notes)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        // Observe error state
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
                viewModel.clearError() // Clear error after showing it
            }
        }

        // Observe success state
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) showSuccess("Notes synced successfully!")
        }

        // Trigger sync when fragment starts
        viewModel.syncNotes()
    }

    private fun showLoading() {
        // Example: Show a ProgressBar
        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        // Example: Hide a ProgressBar
        Toast.makeText(requireContext(), "Done loading.", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
