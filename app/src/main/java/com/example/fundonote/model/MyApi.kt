package com.example.fundonote.model

import retrofit2.Call
import retrofit2.http.GET

//https://console.firebase.google.com/u/0/project/fundonote-e92af/firestore/data/~2Fusers~2Ff1tWny1U8NUUfIOQUkWyzk4Ii7i1~2FNotes~2F4NhxKB4slB6lVG1XkhUC

const val BASE_URL = "https://firestore.googleapis.com/v1/projects/"
const val PROJECT_ID = "fundonote-e92af"
const val API_KEY = "AIzaSyAlo_lIv_3l4XQX--8c7jWcLRwJDC077EE"
interface MyApi {
    @GET("$PROJECT_ID/databases/(default)/documents/users/f1tWny1U8NUUfIOQUkWyzk4Ii7i1/Notes?key=$API_KEY")
      fun getNotes():Call<ArrayList<Notes>>

      //https://firestore.googleapis.com/v1/projects/fundonote-e92af/databases/(default)/documents/users/documents/Notes?apiKey=AIzaSyAlo_lIv_3l4XQX--8c7jWcLRwJDC077EE
}
//https://firestore.googleapis.com/v1/projects/fundonote-e92af/databases/(default)/documents/users/f1tWny1U8NUUfIOQUkWyzk4Ii7i1/Notes?key=AIzaSyAlo_lIv_3l4XQX--8c7jWcLRwJDC077EE