package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.notestakingapp.R
import com.example.notestakingapp.ui.viewmodel.NoteListViewModel
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
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
                viewModel.clearError()
            }
        }
        viewModel.isSuccess.observe(viewLifecycleOwner){isSuccess ->
            if(isSuccess){
                showSuccess("Notes synced successfully!")
            }
        }
        viewModel.syncNotes()
    }
    private fun showLoading(){
        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
    }
    private fun hideLoading(){
        Toast.makeText(requireContext(), "Done Loading", Toast.LENGTH_SHORT).show()
    }
    private fun showError(message: String){
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }
    private fun showSuccess(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}