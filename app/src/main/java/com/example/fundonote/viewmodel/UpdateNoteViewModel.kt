package com.example.fundonote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.AuthListner
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.Notes
import com.example.fundonote.model.ReadNoteAuthListner

class UpdateNoteViewModel(private var noteService: NoteService) : ViewModel() {
    private var _ReadSingleNote = MutableLiveData<ReadNoteAuthListner>()
    private var _UpdateNotes = MutableLiveData < AuthListner >()
    val updateNotes = _UpdateNotes as LiveData<AuthListner>
    val readSigleNote = _ReadSingleNote as LiveData<ReadNoteAuthListner>

    fun readSingleNote(noteId: String) {
        noteService.readSingleNote(noteId) {
            if (it.status) {
                _ReadSingleNote.value = it
            }
        }
    }
    fun updateNote(noteId: String,note: Notes){
        noteService.updateNote(noteId,note){
            if (it.status){
                _UpdateNotes.value = it
            }
        }
    }
}