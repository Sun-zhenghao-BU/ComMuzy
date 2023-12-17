package com.example.commuzy.ui.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commuzy.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.Comment
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
): ViewModel() {
    // state
    private val _uiState = MutableStateFlow(CommunityUiState(posts = emptyList(), isLoading = true))
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

//    fun createPost(content: String) {
//        // 这里调用仓库层的方法来发送帖子
//        viewModelScope.launch {
//            val result = repository.createPost(Post(content = content))
//            // 处理结果，更新帖子列表状态
//            result.fold(
//                onSuccess = { _posts.value = _posts.value + it },
//                onFailure = { /* 处理错误情况 */ }
//            )
//        }
//    }

    fun fetchCommunityScreen() {
        viewModelScope.launch {
//            val sections = repository.getCommunitySections()
//            _uiState.value = CommunityUiState(posts = sections, isLoading = false)
            Log.d("CommunityViewModel", _uiState.value.toString())
        }
    }

    fun createPost(content: String) {

    }
}

data class CommunityUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean
)

data class Post(
    val id: String,
    val author: String,
    val content: String,
    val timestamp: Long,
    val comments: List<Comment>
)

