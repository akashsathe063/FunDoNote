package com.example.fundonote.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentLoginPageBinding
import com.example.fundonote.databinding.FragmentProfileBinding
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService
import com.example.fundonote.viewmodel.ProfileViewModel
import com.example.fundonote.viewmodel.ProfileViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragmet:DialogFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var fireStoreDataBase: FirebaseFirestore
   // private lateinit var   userId:String
    private lateinit var user: User
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      _binding =  FragmentProfileBinding.inflate(inflater,container,false )
//        firebaseAuth = FirebaseAuth.getInstance()
//        fireStoreDataBase = FirebaseFirestore.getInstance()
        profileViewModel = ViewModelProvider(this,ProfileViewModelFactory(UserAuthService())).get(ProfileViewModel::class.java)
                readDataFromFireStore()

        return binding.root
    }
    private fun readDataFromFireStore(){
        user = User(userId = "", userName = "", email = " ", password = " " )
       profileViewModel.profile(user)
        profileViewModel.userProfileStatus.observe(viewLifecycleOwner, Observer {
            if(it.status){
                binding.dialognametv.text = it.user.userName
                binding.dialogEmailtv.text = it.user.email
            }
        })
    }

}