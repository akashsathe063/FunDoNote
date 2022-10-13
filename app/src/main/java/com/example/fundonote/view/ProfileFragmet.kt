package com.example.fundonote.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fundonote.R
import com.example.fundonote.databinding.FragmentProfileBinding
import com.example.fundonote.model.User
import com.example.fundonote.model.UserAuthService
import com.example.fundonote.viewmodel.ProfileUploadViewModel
import com.example.fundonote.viewmodel.ProfileUploadViewModelFactory
import com.example.fundonote.viewmodel.ProfileViewModel
import com.example.fundonote.viewmodel.ProfileViewModelFactory


class ProfileFragmet : DialogFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var fireStoreDataBase: FirebaseFirestore
    // private lateinit var   userId:String
    private lateinit var user: User
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileUploadViewModel: ProfileUploadViewModel
    lateinit var imageUri:Uri


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        firebaseAuth = FirebaseAuth.getInstance()
//        fireStoreDataBase = FirebaseFirestore.getInstance()
//        fireStorage = FirebaseStorage.getInstance()
        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(UserAuthService())
        ).get(ProfileViewModel::class.java)
        readDataFromFireStore()
        profileUploadViewModel = ViewModelProvider(
            this,
            ProfileUploadViewModelFactory(UserAuthService())
        ).get(ProfileUploadViewModel::class.java)
        readDataFromFireStore()
        binding.profileImage.setOnClickListener {
//           Glide
//               .with(this)
//               .load("${selectImage()}")
//               .into(binding.profileImage)
            selectImage()
        }
        binding.btnuploadimg.setOnClickListener {
          //  uploadImage()
            displayProfile()
        }
        binding.btnlogout.setOnClickListener {
            val transaction =
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmaintContainer, LoginPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }
        return binding.root
    }

    private fun readDataFromFireStore() {
        user = User(userId = "", userName = "", email = " ", password = " ")
        profileViewModel.profile(user)
        profileViewModel.userProfileStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                binding.dialognametv.text = it.user.userName
                binding.dialogEmailtv.text = it.user.email
            }
        })
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select picture"), 100)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

           imageUri = data?.data!!

            binding.profileImage.setImageURI(imageUri)
            uploadImage()
        }
    }

    private fun uploadImage() {

        user = User( email =" ", password = " ", userName = " ", image = " ")

        profileUploadViewModel.uploadprofile(user, imageUri = imageUri)
        profileUploadViewModel.userProfileStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                binding.profileImage.setImageURI(user.image.toUri())
                Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()

            } else {

                Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun displayProfile(){
        user = User(userId = "", userName = "", email = " ", password = " ", image = "")
        profileViewModel.displayImage(user)
        profileViewModel.userProfileStatus.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                binding.dialognametv.text = it.user.userName
                binding.dialogEmailtv.text = it.user.email
                var imageUri = it.user.image
                Glide
               .with(this)
               .load(imageUri)
               .into(binding.profileImage)


            }
        })
    }
}