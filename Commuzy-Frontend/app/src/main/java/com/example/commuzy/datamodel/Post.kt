package com.example.commuzy.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val content: String,
    val albumId: Int,
    val albumName: String,
    val timestamp: Long,
    val comments: List<Comment>
)

