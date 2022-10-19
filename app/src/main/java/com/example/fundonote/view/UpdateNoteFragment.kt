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
import com.example.fundonote.HomeFragment
import com.example.fundonote.R
import com.example.fundonote.database.DBHelper
import com.example.fundonote.databinding.FragmentNoteBinding
import com.example.fundonote.databinding.FragmentUpdateNoteBinding
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import com.example.fundonote.viewmodel.UpdateNoteViewModel
import com.example.fundonote.viewmodel.UpdateNoteViewModelFactory


class UpdateNoteFragment : Fragment() {
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!
    lateinit var updateNoteViewModel: UpdateNoteViewModel
    lateinit var noteId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)

        updateNoteViewModel =
            ViewModelProvider(this, UpdateNoteViewModelFactory(NoteService( DBHelper(requireContext())))).get(
                UpdateNoteViewModel::class.java
            )
        noteId = arguments?.getString("noteId").toString()
        readSingleNote()
        updateNote()
        return binding.root
    }

    private fun readSingleNote() {
        Log.d("updatenotefragment", "${noteId}")
        updateNoteViewModel.readSingleNote(noteId)
        updateNoteViewModel.readSigleNote.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                var title: TextView = binding.updatenoteTitle
                var description: TextView = binding.updatenoteContent
//                    binding.noteTitle.setText(it.note.noteTitle)
//                    binding.noteContent.setText(it.note.noteDescription)
                title.text = it.note.noteTitle
                description.text = it.note.noteDescription
            }
        })

    }

    private fun updateNote() {
        binding.btnUpdateNote.setOnClickListener {
            val noteTitle = binding.updatenoteTitle.text.toString()
            val noteDescription = binding.updatenoteContent.text.toString()
            var note =
                Notes(noteId = noteId, noteTitle = noteTitle, noteDescription = noteDescription)
            updateNoteViewModel.updateNote(noteId, note)
            updateNoteViewModel.updateNotes.observe(viewLifecycleOwner, Observer {
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