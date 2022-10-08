package com.example.fundonote.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentRegistrationPageBinding
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService
import com.example.fundonote.viewmodel.RegisterViewModelFactory
import com.example.fundonote.viewmodel.ResgisterViewModel


class RegistrationPageFragment : Fragment() {
    private var _binding: FragmentRegistrationPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerViewModel: ResgisterViewModel
   // private lateinit var userID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationPageBinding.inflate(inflater, container, false)
        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(
                ResgisterViewModel::class.java
            )
        binding.tvAlreadyAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registrationPageFragment_to_loginPageFragment)
        }
        binding.btnregister.setOnClickListener {
            registration()
        }
        return binding.root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private fun registration() {

            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val confirmPass = binding.ConfirmPassEt.text.toString()
            val name = binding.nameEt.text.toString()
            var user = User(userName = name, email = email, password = pass)
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    registerViewModel.registerUser(user)
                    registerViewModel.userRegisterStatus.observe(viewLifecycleOwner, Observer {
                        if (it.status) {
                            Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_registrationPageFragment_to_loginPageFragment)
                        } else {
                            Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(context, "password not matching", Toast.LENGTH_SHORT).show()
            }
        }
    }
