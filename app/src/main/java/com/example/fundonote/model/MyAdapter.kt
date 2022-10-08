package com.example.fundonote.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.R

class MyAdapter(var noteList: ArrayList<Notes>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var allNote = mutableListOf<Notes>().apply {
        Log.d("MyAdapter",noteList.size.toString())
        addAll(noteList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var note: Notes = allNote[position]

        holder.noteTitle.text = note.noteTitle
        holder.noteDescription.text = note.noteDescription
    }

    override fun getItemCount(): Int {
        return allNote.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var noteTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        var noteDescription: TextView = itemView.findViewById(R.id.NoteDescription)
    }


}