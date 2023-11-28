package com.example.commuzy.repository

import com.example.commuzy.database.DatabaseDao
import com.example.commuzy.datamodel.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteAlbumRepository @Inject constructor(private val databaseDao: DatabaseDao) {

    fun isFavoriteAlbum(id: Int): Flow<Boolean> =
        databaseDao.isFavoriteAlbum(id).flowOn(Dispatchers.IO)

    suspend fun favoriteAlbum(album: Album) = withContext(Dispatchers.IO) {
        databaseDao.favoriteAlbum(album)
    }

    suspend fun unFavoriteAlbum(album: Album) = withContext(Dispatchers.IO) {
        databaseDao.unFavoriteAlbum(album)
    }

    fun fetchFavoriteAlbums(): Flow<List<Album>> =
        databaseDao.fetchFavoriteAlbums().flowOn(Dispatchers.IO)

}
