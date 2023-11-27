package com.example.commuzy.datamodel

import com.google.gson.annotations.SerializedName

data class Album(
    val id: Int,
    @SerializedName("album")
    val name: String,
    val year: String,
    val cover: String,
    val artists: String,
    val description: String
)


