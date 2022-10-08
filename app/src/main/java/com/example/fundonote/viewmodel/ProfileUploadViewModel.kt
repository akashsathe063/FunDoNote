package com.example.fundonote.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.AuthListner
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService

class ProfileUploadViewModel(private var userAuthService: UserAuthService):ViewModel() {
    var _ProfileUploadStatus = MutableLiveData<AuthListner>()
    val userProfileStatus = _ProfileUploadStatus as LiveData<AuthListner>
    fun uploadprofile(user: User,imageUri:Uri){
        userAuthService.uploadingProfile(user,imageUri){
            if(it.status){
                _ProfileUploadStatus.value = it
            }
        }
    }
}