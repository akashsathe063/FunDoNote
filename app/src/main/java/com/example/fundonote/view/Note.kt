package com.example.fundonote.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentLoginPageBinding
import com.example.fundonote.databinding.FragmentNoteBinding
import com.example.fundonote.model.AuthListner
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User


class Note : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStoreDataBase: FirebaseFirestore
    lateinit var userId:String

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        saveNote()
        return binding.root
    }
private fun saveNote(){
    binding.btnSaveNote.setOnClickListener {
       val noteTitle = binding.noteTitle.text.toString()
        val noteDescription = binding.noteContent.text.toString()
         userId = firebaseAuth.currentUser?.uid.toString()
        if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
            var documentReference: DocumentReference =
                fireStoreDataBase.collection("notes").document(userId)
            var fireStoreNote = HashMap<String, String>()
            fireStoreNote.put("NoteTitle", noteTitle)
            fireStoreNote.put("NoteDescription", noteDescription)
            fireStoreNote.put("userId",userId)
            documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {
                Toast.makeText(context, "note Save successfully", Toast.LENGTH_SHORT).show()

            })
        }else{
            Toast.makeText(context,"empty field is not allowed",Toast.LENGTH_LONG).show()
        }

    }
}

}