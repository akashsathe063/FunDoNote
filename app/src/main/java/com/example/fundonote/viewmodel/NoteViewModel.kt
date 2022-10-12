package com.example.fundonote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.*

class NoteViewModel(private var noteService: NoteService): ViewModel() {

    private var _UserNoteStatus = MutableLiveData<AuthListner>()
    private var _GetNotes = MutableLiveData<NoteAuthListener>()
    private var _DeleteNotes  = MutableLiveData<AuthListner>()
    private var _UpdateNote = MutableLiveData<NoteAuthListener>()
    val userNoteStatus = _UserNoteStatus as LiveData<AuthListner>
    val getNotes = _GetNotes as LiveData<NoteAuthListener>
    val deleteNotes = _DeleteNotes as LiveData<AuthListner>
    val updateNotes = _UpdateNote as LiveData<NoteAuthListener>
    fun userNote(note: Notes){
        noteService.saveNote(note){
            if(it.status){
                _UserNoteStatus.value = it
            }
        }
    }
    fun getNote(){
        noteService.getNoteData(){
           _GetNotes.value = it
        }
    }
    fun removeNote(noteId:String){
        noteService.removeNote(noteId){
            if(it.status){
              _DeleteNotes.value = it
            }
        }
    }
    fun updateNote(noteId: String){
       noteService.updateNotes(noteId){
            if (it.status){
                _UpdateNote.value = it
            }
       }
    }
}