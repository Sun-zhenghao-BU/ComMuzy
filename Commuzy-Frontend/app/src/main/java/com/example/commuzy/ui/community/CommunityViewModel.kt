package com.example.commuzy.ui.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Post
import com.example.commuzy.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
): ViewModel() {
    // state
    private val _uiState = MutableStateFlow(CommunityUiState(posts = emptyList(), isLoading = false))
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private val _favoriteAlbums = MutableStateFlow<List<Album>>(emptyList())
    val favoriteAlbums: StateFlow<List<Album>> = _favoriteAlbums.asStateFlow()

    init {
        fetchFavoriteAlbums()
        fetchCommunityScreen()
    }

    fun fetchCommunityScreen() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllPosts().collect { postsList ->
                    _uiState.value = CommunityUiState(posts = postsList, isLoading = false)
                }
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Error fetching community screen data", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun createPost(comment: String, albumId: Int, albumName: String) {
        viewModelScope.launch {
            repository.createPost("Anthony", comment, albumId, albumName)
            fetchCommunityScreen()
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.deletePost(post)
            fetchCommunityScreen()
        }
    }

    private fun fetchFavoriteAlbums() {
        viewModelScope.launch {
            repository.getFavoriteAlbums().collect { albums ->
                _favoriteAlbums.value = albums
            }
        }
    }

    suspend fun findAlbumForPost(post: Post): Album {
        return withContext(Dispatchers.IO) {
            repository.getAlbumById(post.albumId)
        }
    }
}

data class CommunityUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean
)


