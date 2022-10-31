package com.example.fundonote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundonote.database.DBHelper
import com.example.fundonote.databinding.FragmentArchiveNoteBinding
import com.example.fundonote.databinding.FragmentSaveNoteBinding
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.view.MyAdapter
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import java.util.Observer


class ArchiveNoteFragment : Fragment() {

    private var _binding: FragmentArchiveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var archiveNoteArrayList: ArrayList<Notes>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentArchiveNoteBinding.inflate(inflater, container, false)
         recyclerView = binding.ArchiveRecyclerView
        noteViewModel = ViewModelProvider(
            this,
            NoteViewModelFactory(NoteService(DBHelper(requireContext())))
        ).get(NoteViewModel::class.java)
       recyclerView.layoutManager = LinearLayoutManager(context)


        recyclerView.setHasFixedSize(true)
        archiveNoteArrayList = arrayListOf<Notes>()

        return binding.root
    }
    fun readArchiveNote() {

    }

}


