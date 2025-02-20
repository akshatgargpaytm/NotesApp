package com.example.notestakingapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notestakingapp.ui.viewmodel.AddNoteViewModel
import com.example.notestakingapp.R
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val viewModel: AddNoteViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSelectImage: Button = view.findViewById(R.id.btnSelectImage)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val statusText: TextView = view.findViewById(R.id.statusText)

        btnSelectImage.setOnClickListener {
            openGallery()
        }

        viewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            progressBar.visibility = if (status == "Uploading...") View.VISIBLE else View.GONE
            statusText.text = status
        }

        // Observe loading state
        viewModel.isAddingNote.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it)
                viewModel.clearError() // Clear error after showing
            }
        }

        // Observe success state
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) showSuccess("Note added successfully!")
        }

        val btnAddNote = view.findViewById<Button>(R.id.btnAddNote)

        // Handle note submission
        btnAddNote.setOnClickListener {
            val edtTitle = view.findViewById<EditText>(R.id.edtTitle)
            val title = edtTitle.text.toString()
            val edtContent = view.findViewById<EditText>(R.id.edtContent)
            val content = edtContent.text.toString()
            viewModel.addNote(title, content)
        }
    }

    private fun showLoading() {
        Toast.makeText(requireContext(), "Adding note...", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoading() {
        Toast.makeText(requireContext(), "Finished.", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val file = File(requireContext().cacheDir, "upload.jpg")
                viewModel.uploadImage(file)
            }
        }
    }
}
