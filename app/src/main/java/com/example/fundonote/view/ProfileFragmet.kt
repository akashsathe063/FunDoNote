package com.example.fundonote.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentDialogFragmentBinding
import com.example.fundonote.databinding.FragmentForgotPasswordBinding


class DialogFragment:DialogFragment() {
    private var _binding: FragmentDialogFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogFragmentBinding.inflate(inflater, container, false)
//        binding.btnlogout.setOnClickListener {
//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.frameLayout, Note())
//            fragmentTransaction.commit()
//        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}