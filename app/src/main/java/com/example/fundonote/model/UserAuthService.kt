package com.example.fundonote.model


import android.app.ProgressDialog
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fundonote.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class UserAuthService() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStoreDataBase: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStoreDataBase = FirebaseFirestore.getInstance()
    }

    fun userRegistration(user: User, listner: (AuthListner) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.userId = firebaseAuth.currentUser?.uid.toString()
                    var documentReference: DocumentReference =
                        fireStoreDataBase.collection("users").document(user.userId)
                    var fireStoreUser = HashMap<String, String>()
                    fireStoreUser.put("email", user.email)
                    fireStoreUser.put("password", user.password)
                    fireStoreUser.put("Name", user.userName)
                    fireStoreUser.put("image", user.image)
                    fireStoreUser.put("userId", user.userId)
                    documentReference.set(fireStoreUser).addOnSuccessListener(OnSuccessListener() {
                        //    Log.d(it.toString(),"user profile is created for"+user.userId)
                        listner(AuthListner(status = true, msg = "User register Succesfully"))
                    })

                } else {

                    listner(AuthListner(status = false, msg = "user register failed"))
                }
            }
    }

    fun userLogin(user: User, listner: (AuthListner) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password).addOnCompleteListener {
            if (it.isSuccessful) {
                listner(AuthListner(status = true, msg = "User Login Succesfully"))
            } else {
                listner(AuthListner(status = false, msg = "User Login failed"))
            }
        }
    }

    fun userForgotPassword(user: User, listner: (AuthListner) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(user.email).addOnCompleteListener {
            if (it.isSuccessful) {
                //Toast.makeText(context, "email sed succesfully to reset your password", Toast.LENGTH_LONG).show()

                listner(AuthListner(status = true, msg = "User Forgot Password Succesfully"))

            } else {
                // Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT) .show()
                listner(AuthListner(status = false, msg = "User Forgot Password failed"))
            }
        }
    }

    fun readFromFireStore(user: User, listner: (ProfileAuthListner) -> Unit) {
        lateinit var userInformation: User
        user.userId = firebaseAuth.currentUser!!.uid
        var documentReference: DocumentReference =
            fireStoreDataBase.collection("users").document(user.userId)
        documentReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                userInformation = User(
                    it.result.getString("userId").toString(),
                    it.result.getString("Name").toString(),
                    it.result.getString("email").toString(),
                    it.result.getString("password").toString(),
                    it.result.getString("image").toString()
                )
//                binding.dialogEmailtv.text =" ${it.result.getString("email").toString()}"
//                binding.dialognametv.text = " ${it.result.getString("Name").toString()}"

                listner(
                    ProfileAuthListner(
                        status = true,
                        msg = "read from fireStore successful",
                        user = userInformation
                    )
                )
            }
        }
    }

    fun uploadingProfile(user: User, imageUri: Uri, listner: (AuthListner) -> Unit) {
//        var imageuri: Uri? = null
        user.userId = firebaseAuth.currentUser?.uid.toString()
        if (imageUri != null) {
            val storageReference =
                FirebaseStorage.getInstance().getReference("Image/${user.userId}/ProfileImage")
            storageReference.putFile(imageUri).addOnSuccessListener {
                val downloadUrl = storageReference.downloadUrl
                downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.addOnSuccessListener {
                            user.image = it.toString()
                            fireStoreDataBase.collection("users").document(user.userId)
                                .update("image", it.toString())

                                .addOnSuccessListener(OnSuccessListener() {
                                    //    Log.d(it.toString(),"user profile is created for"+user.userId)
                                    listner(
                                        AuthListner(
                                            status = true,
                                            msg = "image upload succesfully"
                                        )
                                    )
                                })
                        }
                    }
                }
            }
        } else {
            listner(AuthListner(status = false, msg = "Image Upload failed"))
        }

    }

//    fun saveNote(note: Notes, listner: (AuthListner) -> Unit) {
//        val userId = firebaseAuth.currentUser?.uid.toString()
//        note.noteId = UUID.randomUUID().toString()
//        var fireStoreNote = HashMap<String, String>()
//
//        fireStoreNote.put("NoteTitle", note.noteTitle)
//        fireStoreNote.put("NoteDescription", note.noteDescription)
//        fireStoreNote.put("NoteId", note.noteId)
//        var documentReference: DocumentReference =
//            fireStoreDataBase.collection("users").document(userId).collection("Notes")
//                .document(note.noteId)
//
//        documentReference.set(fireStoreNote).addOnSuccessListener(OnSuccessListener() {
//            listner(AuthListner(status = true, msg = "note save Succesfully"))
//
//        })
//    }

    fun displayImage(user: User, listner: (ProfileAuthListner) -> Unit) {
        lateinit var displayImage: User
        user.userId = firebaseAuth.currentUser?.uid.toString()
        var documentReference: DocumentReference =
            fireStoreDataBase.collection("users").document(user.userId)
        documentReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                displayImage = User(
                    it.result.getString("userId").toString(),
                    it.result.getString("Name").toString(),
                    it.result.getString("email").toString(),
                    it.result.getString("password").toString(),
                    it.result.getString("image").toString()
                )
                listner(
                    ProfileAuthListner(
                        status = true,
                        msg = "read from fireStore successful",
                        user = displayImage
                    )
                )
            }
        }
    }

}








