package com.example.commuzy.database

import androidx.room.*
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Comment
import com.example.commuzy.datamodel.Post
import com.example.commuzy.datamodel.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserInfo)

    @Query("SELECT * FROM UserInfo WHERE id = :userId")
    suspend fun getUserById(userId: String): UserInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriteAlbum(album: Album)

    @Query("SELECT EXISTS(SELECT * FROM Album WHERE id = :id)")
    fun isFavoriteAlbum(id : Int) : Flow<Boolean>

    @Delete
    suspend fun unFavoriteAlbum(album: Album)

    @Query("select * from Album")
    fun fetchFavoriteAlbums(): Flow<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("SELECT * FROM Post ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM Post WHERE albumName = :albumName ORDER BY timestamp DESC")
    fun getPostsByAlbumName(albumName: String): Flow<List<Post>>

    @Query("SELECT * FROM Album WHERE id = :albumId")
    suspend fun getAlbumById(albumId: Int): Album

    @Query("UPDATE Post SET upVotes = upVotes + 1 WHERE id = :postId")
    suspend fun upVotePost(postId: Int)

    @Query("UPDATE Post SET downVotes = downVotes + 1 WHERE id = :postId")
    suspend fun downVotePost(postId: Int)

    @Query("SELECT * FROM comments WHERE postId = :postId")
    fun getCommentsForPost(postId: Int): Flow<List<Comment>>

    @Insert
    fun addCommentToPost(comment: Comment)
}

