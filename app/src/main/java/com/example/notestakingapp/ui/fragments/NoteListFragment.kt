package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notestakingapp.R
import com.example.notestakingapp.ui.adapters.NoteAdapter
import com.example.notestakingapp.ui.viewmodel.NoteListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val viewModel: NoteListViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView and adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotes)
        adapter = NoteAdapter(emptyList()) { note ->
            // Navigate to NoteDetailFragment with note ID
            val bundle = Bundle().apply {
                putInt("noteId", note.id)
            }
            findNavController().navigate(R.id.action_noteListFragment_to_noteDetailFragment, bundle)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Set up Floating Action Button to navigate to AddNoteFragment
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddNote)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        // Observe notes LiveData and update adapter
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.updateNotes(notes)
        }

        // Observe loading state, error state, and success state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
                viewModel.clearError()
            }
        }
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) showSuccess("Notes synced successfully!")
        }

        // Trigger initial sync
        viewModel.syncNotes()
    }

    private fun showLoading() {
        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        Toast.makeText(requireContext(), "Done loading.", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
