package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notestakingapp.R
import com.example.notestakingapp.ui.viewmodel.NoteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment(R.layout.fragment_note_detail){
    private val viewModel: NoteDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        // Observe loading state and toggle ProgressBar
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Load note details
        val noteId = arguments?.getInt("noteId") ?: return
        viewModel.loadNote(noteId)

        view.findViewById<Button>(R.id.btnDeleteNote).setOnClickListener {
            viewModel.deleteNote()
        }

        view.findViewById<Button>(R.id.btnEditNote).setOnClickListener {
            // Navigate to edit screen
        }
    }
}