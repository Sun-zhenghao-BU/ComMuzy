package com.example.commuzy.ui.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commuzy.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
): ViewModel() {
    // state
    private val _uiState = MutableStateFlow(CommunityUiState(posts = emptyList(), isLoading = false))
    val favorites: StateFlow<List<String>> = MutableStateFlow(listOf("Album 1", "Album 2", "Album 3"))
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    fun fetchCommunityScreen() {
        viewModelScope.launch {
//            val sections = repository.getCommunitySections()
//            _uiState.value = CommunityUiState(posts = sections, isLoading = false)
            Log.d("CommunityViewModel", _uiState.value.toString())
        }
    }

    fun createPost(comment: String, album: String) {
        // Assuming you have a method to generate a unique ID for each post
        val newPostId = generateUniqueId()

        // Create a new Post object
        val newPost = Post(
            id = newPostId,
            author = "User", // Replace with the actual user information
            albumName = album,
            content = comment,
            timestamp = System.currentTimeMillis(), // Current timestamp
            comments = emptyList() // Start with an empty list of comments
        )

        // Update the posts list within the UI state
        _uiState.update { currentState ->
            val updatedPosts = currentState.posts + newPost
            currentState.copy(posts = updatedPosts)
        }
    }
}

// Generate a unique ID for each post (placeholder implementation)
fun generateUniqueId(): String {
    return UUID.randomUUID().toString()
}

data class CommunityUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean
)

data class Post(
    val id: String,
    val author: String,
    val albumName: String,
    val content: String,
    val timestamp: Long,
    val comments: List<Comment>
)

