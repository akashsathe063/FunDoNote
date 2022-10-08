package com.example.fundonote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundonote.model.NoteService
import com.example.fundonote.model.UserAuthService

class NoteViewModelFactory(private var noteService: NoteService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(noteService) as T
    }
}