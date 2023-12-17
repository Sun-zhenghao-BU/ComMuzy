package com.example.commuzy.ui.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.commuzy.R
import com.example.commuzy.ui.home.LoadingSection



@Composable
fun CommunityScreen(viewModel: CommunityViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    CommunityScreenContent(uiState, favorites)
}

@Composable
fun CommunityScreenContent(uiState: CommunityUiState, favorites: List<String>) {
    var showPostCreator by remember { mutableStateOf(false) }

    Column {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item { CommunityScreenHeader(onAddPostClicked = { showPostCreator = true }) }
            if (uiState.isLoading) {
                item { LoadingSection(stringResource(id = R.string.screen_loading)) }
            } else {
                items(uiState.posts) { post -> PostSection(post) }
            }
        }
    }
}

@Composable
fun CommunityScreenHeader(onAddPostClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.menu_community),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        IconButton(
            onClick = onAddPostClicked,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add Post",
                tint = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun PostSection(post: Post) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = post.id,
            style = MaterialTheme.typography.h5.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.White
        )
    }
}

@Composable
fun PostCreationComponent(
    songs: List<String>, // 假设这是歌曲列表
    onPostCreated: (String, String) -> Unit
) {
    var selectedSong by remember { mutableStateOf(songs.firstOrNull() ?: "") }
    var comment by remember { mutableStateOf("") }

    Column {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { /* 用于处理菜单关闭逻辑 */ }
        ) {
            songs.forEach { song ->
                DropdownMenuItem(onClick = { selectedSong = song }) {
                    Text(song)
                }
            }
        }

        TextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Share your insights now") }
        )

        Button(
            onClick = { onPostCreated(selectedSong, comment) },
            enabled = comment.isNotBlank()
        ) {
            Text("Post")
        }
    }
}
