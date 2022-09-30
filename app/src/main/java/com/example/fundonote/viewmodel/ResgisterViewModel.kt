package com.example.fundonote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.AuthListner
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService

class ResgisterViewModel(private var userAuthService: UserAuthService):ViewModel() {

    private var _UserRegisterStatus = MutableLiveData<AuthListner>()
    val userRegisterStatus = _UserRegisterStatus as LiveData<AuthListner>
    fun registerUser(user: User){
        userAuthService.userRegistration(user){
            if(it.status){
                _UserRegisterStatus.value = it
            }
        }
    }
}