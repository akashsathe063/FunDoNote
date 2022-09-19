package com.example.fundonote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fundonote.databinding.FragmentLoginPageBinding
import com.google.firebase.auth.FirebaseAuth


class LoginPageFragment : Fragment() {
    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginPageFragment_to_registrationPageFragment)
        }

        binding.btnlogin.setOnClickListener {
            val email = binding.usernameEt.text.toString()
            val pass = binding.passwordEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        findNavController().navigate(R.id.action_loginPageFragment_to_mainFragment)
                    } else {
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            } else {
                Toast.makeText(context, "Empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}