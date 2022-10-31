package com.example.fundonote.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.database.DBHelper
import com.example.fundonote.databinding.FragmentArchiveNoteBinding
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory


class ArchiveNoteFragment : Fragment() {

    private var _binding: FragmentArchiveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var archiveNoteArrayList: ArrayList<Notes>
    private lateinit var unArchiveNoteList: ArrayList<Notes>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteId: String
    private lateinit var notes: Notes
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArchiveNoteBinding.inflate(inflater, container, false)

        recyclerView = binding.ArchiverecyclerView

        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NoteService(DBHelper(requireContext())))
        ).get(NoteViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)
        recyclerView.setHasFixedSize(true)
        archiveNoteArrayList = arrayListOf<Notes>()
        unArchiveNoteList = arrayListOf<Notes>()

        readArchiveNote()

        return binding.root
    }

    fun readArchiveNote() {
        //  archiveNoteArrayList.clear()
        noteViewModel.getNote()
        noteViewModel.getNotes.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                archiveNoteArrayList = it.noteList
                //   newNoteArrayList.addAll(tempNoteArrayList)
                //    archiveNoteList.addAll(noteArrayList)
                archiveNoteArrayList.forEach {
                    if (it.isArchive == false) {
                        unArchiveNoteList.add(it)
                        //           archiveNote.readArchiveNote(archiveNoteList)
                    } else {
                        archiveNoteArrayList.add(it)
                        Log.d("ArhiveNoteFragment", "${archiveNoteArrayList.size}")
                        recyclerView.adapter = MyAdapter(archiveNoteArrayList, requireContext())
                    }
                }

            }
        })
    }

}


