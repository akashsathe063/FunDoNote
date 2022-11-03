package com.example.fundonote.model

import android.util.Log
import com.example.fundonote.database.DBHelper
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NoteService(private val dbHelper: DBHelper) {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStoreDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var notes: Notes
    private lateinit var documentReference: DocumentReference


    init {
        //    firebaseAuth = FirebaseAuth.getInstance()
        //    fireStoreDataBase = FirebaseFirestore.getInstance()
    }

    fun saveNote(note: Notes, listner: (AuthListner) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid.toString()
        var fireStoreNote = HashMap<String, String>()

        fireStoreNote.put("NoteTitle", note.noteTitle)
        fireStoreNote.put("NoteDescription", note.noteDescription)
        fireStoreNote.put("isArchive", note.isArchive.toString())

        firebaseAuth.currentUser?.let {
            var documentReference: DocumentReference =
                fireStoreDataBase.collection("users").document(it.uid).collection("Notes")
                    .document()
            // note.userId = it.uid
            note.noteId = documentReference.id
            fireStoreNote.put("NoteId", note.noteId)
            //    fireStoreNote.put("UserId", note.userId)
            documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {

                fireStoreDataBase.collection("users").document(userId).collection("Notes")
                    .document(note.noteId).set(fireStoreNote)
                dbHelper.addNotes(note)
                listner(AuthListner(status = true, msg = "note save Succesfully"))

            })
        }

    }

    fun getNoteData(listner: (NoteAuthListener) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        fireStoreDataBase.collection("users").document(userId).collection("Notes").get()
            .addOnCompleteListener {

                val noteList = ArrayList<Notes>()
                if (it.isSuccessful) {
                    for (document in it.result) {
                        var archiveDoc = document["isArchive"].toString()
                        var isArchive: Boolean
                        isArchive = (archiveDoc == "true")
                        val userNote: Notes = Notes(
                            //  document["UserId"].toString(),
                            document["NoteId"].toString(),
                            document["NoteTitle"].toString(),
                            document["NoteDescription"].toString(),
                            isArchive
                        )
                        noteList.add(userNote)
                        // dbHelper.getAllNotes()
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
                    dbHelper.deleteNote(noteId)
                    listener(
                        AuthListner(
                            status = true,
                            "note delete Successfully"
                        )
                    )
                }
            }
    }

    fun readSingleNote(noteId: String, listner: (ReadNoteAuthListner) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        fireStoreDataBase = FirebaseFirestore.getInstance()
        fireStoreDataBase.collection("users").document(userId).collection("Notes").document(noteId)
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    var archiveDoc = it.result.getString("isArchive").toString()
                    var isArchive: Boolean
                    isArchive = archiveDoc == "true"
                    val userNote: Notes = Notes(
                        //       it.result.getString("UserId").toString(),
                        it.result.getString("NoteId").toString(),
                        it.result.getString("NoteTitle").toString(),
                        it.result.getString("NoteDescription").toString(),
                        isArchive


                    )
                    dbHelper.readSingleNote(noteId)
                    listner(
                        ReadNoteAuthListner(
                            note = userNote,
                            status = true,
                            msg = "fetch note Successfully"
                        )
                    )


                }

                //        Log.d("NoteService", noteList.size.toString())
            }

    }

    fun updateNote(noteId: String, notes: Notes, listner: (AuthListner) -> Unit) {
        var userId: String = firebaseAuth.currentUser?.uid.toString()
        var fireStoreNote = HashMap<String, String>()
        fireStoreNote.put("NoteTitle", notes.noteTitle)
        fireStoreNote.put("NoteDescription", notes.noteDescription)
        fireStoreNote.put("NoteId", notes.noteId)
        fireStoreNote.put("isArchive", notes.isArchive.toString())
        //  fireStoreNote.put("isArchive","1")
        var documentReference: DocumentReference =
            fireStoreDataBase.collection("users").document(userId).collection("Notes")
                .document(noteId)

        documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {
            dbHelper.updateNote(notes)
            listner(AuthListner(status = true, msg = "note save Succesfully"))

        })

    }


}








