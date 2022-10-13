package com.example.fundonote.model

import android.util.Log
import com.example.fundonote.view.MyAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteService() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStoreDataBase: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var notes: Notes
    private lateinit var myAdapter: MyAdapter


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
                        val noteTitle: String = document["NoteTitle"].toString()
                        val noteContent: String = document["NoteDescription"].toString()
                        val noteId: String = document["NoteId"].toString()
                        val userNote: Notes = Notes(
                            noteId = noteId,
                            noteTitle = noteTitle,
                            noteDescription = noteContent
                        )
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

    fun removeNote(noteId: String, listener: (AuthListner) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        fireStoreDataBase.collection("users").document(userId).collection("Notes").document(noteId)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener(
                        AuthListner(
                            status = true,
                            "note delete Successfully"
                        )
                    )
                }
            }
    }

    fun readSingleNote(noteId: String, listner: (UpdateNoteAuthListner) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        fireStoreDataBase.collection("users").document(userId).collection("Notes").document(noteId)
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    val userNote: Notes = Notes(
                        it.result.getString("NoteId").toString(),
                        it.result.getString("NoteTitle").toString(),
                        it.result.getString("NoteDescription").toString()

                    )
                    listner(
                        UpdateNoteAuthListner(
                            note = userNote,
                            status = true,
                            msg = "fetch note Successfully"
                        )
                    )


                }

                //        Log.d("NoteService", noteList.size.toString())
            }

    }

    fun updateNote(noteId: String, listner: (AuthListner) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        var fireStoreNote = HashMap<String, String>()
        fireStoreNote.put("NoteTitle", notes.noteTitle)
        fireStoreNote.put("NoteDescription", notes.noteDescription)
        var documentReference: DocumentReference =
            fireStoreDataBase.collection("users").document(userId).collection("Notes")
                .document(noteId)

        documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {
            listner(AuthListner(status = true, msg = "note save Succesfully"))

        })

    }
}








