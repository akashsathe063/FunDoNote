package com.example.fundonote.view

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentLoginPageBinding
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService
import com.example.fundonote.viewmodel.LoginViewModel
import com.example.fundonote.viewmodel.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginPageFragment : Fragment() {
    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this,LoginViewModelFactory(UserAuthService())).get(LoginViewModel::class.java)
         firebaseAuth = FirebaseAuth.getInstance()
        //configure google signIn
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("690706288664-8vuvfdh45aeb1al95s4t1q3hh20892ag.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it,gso) }!!

        binding.btngoogleSignIn.setOnClickListener{
            signIn()
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginPageFragment_to_registrationPageFragment)
        }

        binding.tvForgetPass.setOnClickListener {
//           // findNavController().navigate(R.id.action_loginPageFragment_to_forgotPasswordFragment)
//            Navigation.findNavController(it).navigate(R.id.action_loginPageFragment_to_forgotPasswordFragment)
            forgotPass()

        }

          login()
        return binding.root
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //result returned from launching the intent from GoogleSignInApi.getSignInIntent(...)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //googleSign In was succesful authenticate with firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG,"firebaseAuthWithGoogle:"+ account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){
                Log.w(ContentValues.TAG,"Google sign in failed",e)
            }
        }
    }
    private fun  firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener{
                if(it.isSuccessful){

                    Toast.makeText(context,"google sign in succesful",Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginPageFragment.requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                }else{
                    Log.w(ContentValues.TAG,"SignWithCredential:Failure",it.exception)

                }

            }
    }
    companion object{
        const val RC_SIGN_IN = 1001

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   private fun login(){
       binding.btnlogin.setOnClickListener {
           val email = binding.usernameEt.text.toString()
           val pass = binding.passwordEt.text.toString()
            var user = User(userName = " ", email = email, password = pass)
           if (email.isNotEmpty() && pass.isNotEmpty()) {
               loginViewModel.LoginUser(user)
               loginViewModel.userLoginStatus.observe(viewLifecycleOwner, Observer {
                   if (it.status) {
                       Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
                       val intent = Intent(this@LoginPageFragment.requireContext(), HomeActivity::class.java)
                       startActivity(intent)
                   } else {
                       Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
                   }
               })
           } else {
               Toast.makeText(context, "Empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
           }
       }
   }
    private fun forgotPass(){

            val email = binding.usernameEt.text.toString()
            var user = User(userName = "", email = email, password = "")
            if(email.isNotEmpty()){
                loginViewModel.forgotPassword(user)
                loginViewModel.forgotPasswordStatus.observe(viewLifecycleOwner, Observer {
                    if(it.status){
                        Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()

                    }else{
                        Toast.makeText(context, it.msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }else{
                Toast.makeText(context, "Empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

