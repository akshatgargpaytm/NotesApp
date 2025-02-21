package com.example.notestakingapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notestakingapp.R
import com.example.notestakingapp.ui.viewmodel.AddNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val viewModel: AddNoteViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 1
    private var imageFile: File? = null
    private var newNoteId: Int? = null

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

        // Observe image upload status
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

            // 1. Add the note to the database
            viewModel.addNote(title, content)

            // 2. Upload the image if selected (after note insertion)
            newNoteId?.let { noteId ->
                imageFile?.let { file ->
                    viewModel.uploadImage(file, noteId)
                }
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
    override fun onDestroyView() {
        super.onDestroyView()

        // Show Action Bar when leaving this fragment
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Convert the image URI to a File
                imageFile = uriToFile(uri)
                Toast.makeText(requireContext(), "Image selected: ${imageFile?.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "upload.jpg")
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}
