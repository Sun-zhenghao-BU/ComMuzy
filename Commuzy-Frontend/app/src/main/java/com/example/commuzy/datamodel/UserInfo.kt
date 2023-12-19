package com.example.commuzy.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey val id: String,
    val name: String,
    val email: String
)