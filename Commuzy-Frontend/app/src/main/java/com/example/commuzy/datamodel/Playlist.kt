package com.example.commuzy.datamodel

import com.google.gson.annotations.SerializedName

data class Playlist(
    @SerializedName("id")
    val albumId: Int,
    val songs: List<Song>
)

