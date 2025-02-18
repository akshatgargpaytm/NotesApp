package com.example.notestakingapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.notestakingapp.R
import com.example.notestakingapp.ui.viewmodel.NoteListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteDetailFragment : Fragment(R.layout.fragment_note_list){
    private val viewModel: NoteListViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notes.observe(viewLifecycleOwner){
            notes ->
        }
        viewModel.isLoading.observe(viewLifecycleOwner){isLoading ->
            if(isLoading){
                progressBar.visibility = View.VISIBLE
            }
            else{

            }
        }
        viewModel.syncNotes()
    }
}