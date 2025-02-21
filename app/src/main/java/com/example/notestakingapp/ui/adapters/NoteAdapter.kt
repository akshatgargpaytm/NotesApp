package com.example.notestakingapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestakingapp.data.local.entity.NoteEntity
import com.example.notestakingapp.databinding.ItemNoteBinding

class NoteAdapter(
    private var notes: List<NoteEntity>,
    private val onItemClick: (NoteEntity) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteEntity) {
            binding.tvTitle.text = note.title
            binding.tvContent.text = note.content
            binding.root.setOnClickListener { onItemClick(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    // New function to update the dataset
    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(newNotes: List<NoteEntity>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
