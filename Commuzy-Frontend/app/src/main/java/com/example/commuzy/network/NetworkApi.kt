package com.example.commuzy.network

import com.example.commuzy.datamodel.Playlist
import com.example.commuzy.datamodel.Section
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    @GET("feed")
    fun getHomeFeed(): Call<List<Section>>

    @GET("playlist/{id}")
    fun getPlaylist(@Path("id") id: Int): Call<Playlist>
}