package com.example.fundonote.model

interface DeleteListner {
    fun noteDeleted(deleteOnClick:Boolean,notes: Notes)
}