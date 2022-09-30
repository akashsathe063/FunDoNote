package com.example.fundonote.model


import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserAuthService() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireStoreDataBase: FirebaseFirestore

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStoreDataBase = FirebaseFirestore.getInstance()
    }

    fun userRegistration(user: User, listner: (AuthListner) -> Unit) {

        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    user.userId = firebaseAuth.currentUser?.uid.toString()
                    var documentReference: DocumentReference = fireStoreDataBase.collection("users").document(user.userId)
                    var  fireStoreUser =  HashMap<String, String>()
                    fireStoreUser.put("email",user.email)
                    fireStoreUser.put("password",user.password)
                    fireStoreUser.put("Name",user.userName)
                    documentReference.set(fireStoreUser).addOnSuccessListener(OnSuccessListener(){
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
}