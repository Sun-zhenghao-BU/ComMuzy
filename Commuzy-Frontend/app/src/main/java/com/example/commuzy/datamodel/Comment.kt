package com.example.commuzy.datamodel

import androidx.room.PrimaryKey

data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val content: String,
)
