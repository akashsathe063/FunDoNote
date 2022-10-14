package com.example.fundonote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundonote.model.NoteService

class UpdateNoteViewModelFactory (private var noteService: NoteService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateNoteViewModel(noteService) as T
    }
}