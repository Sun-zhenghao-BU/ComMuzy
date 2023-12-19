package com.example.commuzy.repository

import com.example.commuzy.database.DatabaseDao
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommunityRepository @Inject constructor(
    private val databaseDao: DatabaseDao
) {
    fun getAllPosts(): Flow<List<Post>> = databaseDao.getAllPosts()

    suspend fun createPost(author: String, content: String, albumId:Int, albumName: String) {
        withContext(Dispatchers.IO) {
            val newPost = Post(
                author = author,
                content = content,
                albumId = albumId,
                albumName = albumName,
                timestamp = System.currentTimeMillis(),
                comments = emptyList()
            )
            databaseDao.insertPost(newPost)
        }
    }

    suspend fun deletePost(post: Post) {
        withContext(Dispatchers.IO) {
            databaseDao.deletePost(post)
        }
    }

    suspend fun getAlbumById(albumId: Int): Album {
        return withContext(Dispatchers.IO) {
            databaseDao.getAlbumById(albumId)
        }
    }

    suspend fun upVotePost(postId: Int) {
        withContext(Dispatchers.IO) {
            databaseDao.upVotePost(postId)
        }
    }

    suspend fun downVotePost(postId: Int) {
        withContext(Dispatchers.IO) {
            databaseDao.downVotePost(postId)
        }
    }

    fun getFavoriteAlbums(): Flow<List<Album>> = databaseDao.fetchFavoriteAlbums()
}