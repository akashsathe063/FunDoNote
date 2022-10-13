package com.example.fundonote.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundonote.R
import com.example.fundonote.HomeFragment
import com.example.fundonote.databinding.FragmentNoteBinding
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory


class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    lateinit var noteViewModel: NoteViewModel
    lateinit var noteId: String

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NoteService())
        ).get(NoteViewModel::class.java)
        noteId = arguments?.getString("noteId").toString()
        readSingleNote()
        saveNote()
        updateNote()
        return binding.root
    }

    private fun saveNote() {
        binding.btnSaveNote.setOnClickListener {
            val noteTitle = binding.noteTitle.text.toString()
            val noteDescription = binding.noteContent.text.toString()
            var note = Notes(noteId = "", noteTitle = noteTitle, noteDescription = noteDescription)
            if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                noteViewModel.userNote(note)
                noteViewModel.userNoteStatus.observe(viewLifecycleOwner, Observer {
                    if (it.status) {
                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                        val transaction =
                            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmaintContainer, HomeFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    } else {
                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "empty field is not allowed", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun readSingleNote() {

        //fetch noteId from adapter to notefragment()


        Log.d("notefragment", "${noteId}")
        noteViewModel.readSingleNote(noteId)
        noteViewModel.readSigleNote.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                var title: TextView = binding.noteTitle
                var description: TextView = binding.noteContent
//                    binding.noteTitle.setText(it.note.noteTitle)
//                    binding.noteContent.setText(it.note.noteDescription)
                title.text = it.note.noteTitle
                description.text = it.note.noteDescription
            }
        })

    }

    private fun updateNote() {
        binding.btnSaveNote.setOnClickListener {
            noteViewModel.updateNote(noteId)
            noteViewModel.updateNotes.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                    val transaction =
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmaintContainer, HomeFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                } else {
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}