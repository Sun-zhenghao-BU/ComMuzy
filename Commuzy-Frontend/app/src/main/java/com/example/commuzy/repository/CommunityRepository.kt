package com.example.commuzy.repository

import com.example.commuzy.database.DatabaseDao
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Post
import com.example.commuzy.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val networkApi: NetworkApi,
    private val databaseDao: DatabaseDao
) {
    fun getAllPosts(): Flow<List<Post>> = databaseDao.getAllPosts()

    suspend fun createPost(author: String, content: String, albumName: String) {
        withContext(Dispatchers.IO) {
            val newPost = Post(
                author = author,
                content = content,
                albumName = albumName,
                timestamp = System.currentTimeMillis(),
                comments = emptyList()
            )
            databaseDao.insertPost(newPost)
        }
    }

    fun getFavoriteAlbums(): Flow<List<Album>> = databaseDao.fetchFavoriteAlbums()
}