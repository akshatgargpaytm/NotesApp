package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notestakingapp.R
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.databinding.FragmentNoteDetailBinding
import com.example.notestakingapp.ui.viewmodel.NoteDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!! // Safe access to binding

    private val viewModel: NoteDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = binding.progressBar

        // Observe loading state and toggle ProgressBar
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Load note details
        val noteId = arguments?.getInt("noteId") ?: return
        viewModel.loadNote(noteId)


        binding.btnDeleteNote.setOnClickListener {
            viewModel.deleteNote()
            findNavController().popBackStack() // Navigate back after deleting
        }

        binding.btnEditNote.setOnClickListener {
            viewModel.updateNote(
                NoteEntity(
                    id = noteId,
                    title = binding.etTitle.text.toString(),
                    content = binding.etContent.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
