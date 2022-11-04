package com.example.fundonote.model

import java.lang.reflect.Field

data class Documents(
    val createTime : String,
    val fields : Fields,
    val name : String,
    val updateTime : String
)
