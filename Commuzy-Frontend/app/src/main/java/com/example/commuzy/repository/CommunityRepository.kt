package com.example.commuzy.repository

import com.example.commuzy.datamodel.Playlist
import com.example.commuzy.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getPlaylist(id: Int): Playlist =
        withContext(Dispatchers.IO) {
            networkApi.getPlaylist(id).execute().body() ?: Playlist(id, listOf())
        }
}