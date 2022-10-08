package com.example.fundonote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundonote.model.UserAuthService

class ProfileUploadViewModelFactory(private var userAuthService: UserAuthService) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileUploadViewModel(userAuthService) as T
    }
}