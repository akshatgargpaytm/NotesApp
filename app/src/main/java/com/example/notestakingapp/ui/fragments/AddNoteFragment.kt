package com.example.notestakingapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notestakingapp.R
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.ui.viewmodel.AddNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val viewModel: AddNoteViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 1
    private var imageFile: Uri? = null  // Using Uri for simplicity; image handling is optional.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val btnSelectImage: Button = view.findViewById(R.id.btnSelectImage)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val statusText: TextView = view.findViewById(R.id.statusText)
        val btnAddNote: Button = view.findViewById(R.id.btnAddNote)
        val edtTitle: EditText = view.findViewById(R.id.edtTitle)
        val edtContent: EditText = view.findViewById(R.id.edtContent)

        btnSelectImage.setOnClickListener { openGallery() }

        // Observe upload status (if applicable)
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
                viewModel.clearError()
            }
        }

        // Observe success state
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) showSuccess("Note added successfully!")
        }

        // Handle note submission
        btnAddNote.setOnClickListener {
            val title = edtTitle.text.toString()
            val content = edtContent.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                val imageUrlString = imageFile?.toString()
                //Convert url to string to store
                viewModel.addNote(title, content, imageUrlString)
                // Navigate back to the NoteListFragment to refresh the list
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Title and Content cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
                imageFile = uri
                Toast.makeText(requireContext(), "Image selected.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}
