package com.example.notestakingapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notestakingapp.data.local.entity.NoteEntity

class NoteAdapter (
    private val notes: List<NoteEntity>,
    private val onItemclick: (NoteEntity) -> Unit
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){
    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(note: NoteEntity){
            binding.tvTitle.text = note.title
            binding.tvContent.text = note.content
            binding.root.setOnClickListener{ onItemclick(note) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder{
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int){
        holder.bind(notes[position])
    }



    override fun getItemCount(): notes.size

}