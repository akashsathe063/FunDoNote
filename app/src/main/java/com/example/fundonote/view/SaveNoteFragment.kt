package com.example.fundonote

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
import com.example.fundonote.databinding.FragmentNoteBinding
import com.example.fundonote.databinding.FragmentProfileBinding
import com.example.fundonote.databinding.FragmentSaveNoteBinding
import com.example.fundonote.model.MyAdapter
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.model.User
import com.example.fundonote.viewmodel.NoteViewModel
import com.example.fundonote.viewmodel.NoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import java.util.*
import kotlin.collections.ArrayList


class SaveNote : Fragment() {
    private var _binding: FragmentSaveNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView : RecyclerView
    private lateinit var noteArrayList : ArrayList<Notes>
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: MyAdapter
    private lateinit var dataBase:FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveNoteBinding.inflate(inflater, container, false)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService())).get(NoteViewModel::class.java)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        noteArrayList = arrayListOf<Notes>()

        noteAdapter = MyAdapter(noteArrayList)

        recyclerView.adapter = noteAdapter

        readNote()
     //   getNote()
        return binding.root
    }
      fun readNote(){
           noteViewModel.getNote()
          noteViewModel.getNotes.observe(viewLifecycleOwner, Observer {
              if (it.status){
                  Log.d("SaveNoteFragment",it.noteList.size.toString())
                  Toast.makeText(context,it.msg, Toast.LENGTH_SHORT).show()
                  recyclerView.adapter = MyAdapter(it.noteList)
              }
          })
      }

//       fun getNote(){
//           val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
//           dataBase = FirebaseFirestore.getInstance()
//           dataBase.collection("users").document(userId).collection("Notes").
//                   addSnapshotListener(object : EventListener<QuerySnapshot>{
//                       override fun onEvent(
//                           value: QuerySnapshot?,
//                           error: FirebaseFirestoreException?
//                       ) {
//                           if (error != null){
//                               Log.e("firestore error", error.message.toString())
//                               return
//                           }
//                           for(documentChanges:DocumentChange  in value?.documentChanges!!){
//                               if(documentChange.type == DocumentChange.Type.ADDED){
//
//                                   val userNote:Notes = documentChange.document.toObject(Notes :: class.java)
//                                   Log.d("get notes",userNote.noteTitle)
//                                   noteArrayList.add(documentChange.document.toObject(Notes::class.java))
//                               }
//                           }
//
//                           noteAdapter.notifyDataSetChanged()
//                       }
//
//                   })
//       }

    }




