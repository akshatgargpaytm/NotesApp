package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        val noteId = arguments?.getInt("noteId", -1) ?: -1

        if(noteId == -1){ //Invalid noteId
            Toast.makeText(requireContext(), "Error: Note not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack() //Navigate back
            return
        }
        viewModel.loadNote(noteId)


        binding.btnDeleteNote.setOnClickListener {
            if (noteId <= 0) { // Prevent deletion of invalid notes
                Toast.makeText(requireContext(), "Error: Cannot delete note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.deleteNote(noteId)
            Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack() // Navigate back after deleting
        }

        binding.btnEditNote.setOnClickListener {
            val updatedTitle = binding.etTitle.text.toString().trim()
            val updatedContent = binding.etContent.text.toString().trim()

            if(noteId<=0){ //Invalid noteId
                Toast.makeText(requireContext(), "Error: Cannot update note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(updatedTitle.isEmpty() || updatedContent.isEmpty()){
                Toast.makeText(requireContext(), "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            viewModel.updateNote(
                NoteEntity(
                    id = noteId,
                    title = updatedTitle,
                    content = updatedContent
                )
            )
            Toast.makeText(requireContext(),"Note Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
