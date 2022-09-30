package com.example.fundonote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonote.model.AuthListner
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService

class LoginViewModel (private var userAuthService: UserAuthService): ViewModel() {

    private var _UserLoginStatus = MutableLiveData<AuthListner>()
    private var _ForgotPasswordStatus = MutableLiveData<AuthListner>()
    val userLoginStatus = _UserLoginStatus as LiveData<AuthListner>
    val forgotPasswordStatus = _ForgotPasswordStatus as LiveData<AuthListner>
    fun LoginUser(user: User){
        userAuthService.userLogin(user){
            if(it.status){
                _UserLoginStatus.value = it
            }
        }
    }
    fun forgotPassword(user:User){
        userAuthService.userForgotPassword(user){
            if (it.status){
                _ForgotPasswordStatus.value = it
            }
        }
    }
}