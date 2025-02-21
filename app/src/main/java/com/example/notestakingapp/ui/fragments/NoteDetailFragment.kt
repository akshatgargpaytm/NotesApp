package com.example.notestakingapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.databinding.FragmentNoteDetailBinding
import com.example.notestakingapp.ui.viewmodel.NoteDetailViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {
    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteDetailViewModel by viewModels()
    private var noteId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getInt("noteId", -1) ?: -1
        if (noteId == -1) {
            Toast.makeText(requireContext(), "Error: Note not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        // ✅ Load the Note
        viewModel.loadNote(noteId)

        // ✅ Observe the Note Data
        viewModel.note.observe(viewLifecycleOwner) { note ->
            if (note != null) {
                binding.etTitle.setText(note.title)
                binding.etContent.setText(note.content)
            }
        }

        // ✅ Observe Loading State
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // ✅ Update Note and Navigate Back
        binding.btnEditNote.setOnClickListener {
            val updatedTitle = binding.etTitle.text.toString().trim()
            val updatedContent = binding.etContent.text.toString().trim()

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(requireContext(), "Title and Content cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedNote = NoteEntity(id = noteId, title = updatedTitle, content = updatedContent)
            viewModel.updateNote(updatedNote)

            Toast.makeText(requireContext(), "Note Updated", Toast.LENGTH_SHORT).show()

            // ✅ Navigate back to the Note List screen
            findNavController().popBackStack()
        }

        // ✅ Delete Note
        binding.btnDeleteNote.setOnClickListener {
            viewModel.deleteNote(noteId)
            Toast.makeText(requireContext(), "Note Deleted", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack() // Navigate back after deleting
        }
    }
    @SuppressLint("ServiceCast")
    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
