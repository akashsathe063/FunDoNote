package com.example.fundonote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.AuthListner
import com.example.fundonote.model.ProfileAuthListner
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService

class ProfileViewModel(private var userAuthService: UserAuthService):ViewModel() {
     var _ProfileStatus = MutableLiveData<ProfileAuthListner>()
    val userProfileStatus = _ProfileStatus as LiveData<ProfileAuthListner>
    fun profile(user: User){
        userAuthService.readFromFireStore(user){
            if(it.status){
                _ProfileStatus.value = it
            }
        }
    }
}