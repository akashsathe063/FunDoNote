package com.example.fundonote.model

import android.nfc.Tag
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import com.google.firebase.firestore.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteService() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStoreDataBase: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var notes: Notes
    private lateinit var myAdapter: MyAdapter
    //   private lateinit

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStoreDataBase = FirebaseFirestore.getInstance()
    }

    fun saveNote(note: Notes, listner: (AuthListner) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid.toString()
        note.noteId = UUID.randomUUID().toString()
        var fireStoreNote = HashMap<String, String>()

        fireStoreNote.put("NoteTitle", note.noteTitle)
        fireStoreNote.put("NoteDescription", note.noteDescription)
        fireStoreNote.put("NoteId", note.noteId)
        var documentReference: DocumentReference =
            fireStoreDataBase.collection("users").document(userId).collection("Notes")
                .document(note.noteId)

        documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {
            listner(AuthListner(status = true, msg = "note save Succesfully"))

        })
    }

    fun getNoteData(listner: (NoteAuthListener) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        fireStoreDataBase.collection("users").document(userId).collection("Notes").get()
            .addOnCompleteListener {
                val noteList = ArrayList<Notes>()
                if (it != null && it.isSuccessful) {
                    for (document in it.result) {
                        val noteTitle:String = document["NoteTitle"].toString()
                        val noteContent:String = document["NoteDescription"].toString()
                        val noteId:String = document["NoteId"].toString()
                        val userNote: Notes = Notes(noteId = noteId,noteTitle = noteTitle, noteDescription = noteContent)
                        noteList.add(userNote)
                    }

                    Log.d("NoteService", noteList.size.toString())
                }
                listner(
                    NoteAuthListener(
                        noteList = noteList,
                        status = true,
                        msg = "fetch note Successfully"
                    )
                )

            }
    }
}






