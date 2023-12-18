package com.example.commuzy.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.commuzy.R
import com.example.commuzy.ui.home.LoadingSection

@Composable
fun CommunityScreen(viewModel: CommunityViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    CommunityScreenContent(uiState, favorites, viewModel)
}

@Composable
fun CommunityScreenContent(uiState: CommunityUiState, favorites: List<String>, viewModel: CommunityViewModel) {
    var showPostCreator by remember { mutableStateOf(false) }

    Column {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item { CommunityScreenHeader(onAddPostClicked = { showPostCreator = true }) }
            if (uiState.isLoading) {
                item { LoadingSection(stringResource(id = R.string.screen_loading)) }
            } else {
                items(uiState.posts) { post ->
                    PostSection(post)
                }
            }
        }
    }

    if (showPostCreator) {
        Dialog(onDismissRequest = { showPostCreator = false }) {
            PostCreationComponent(
                albums = favorites,
                onPostCreated = { comment, selectedAlbum, ->
                    viewModel.createPost(selectedAlbum, comment) // Call ViewModel's createPost function to create post
                    showPostCreator = false
                },
                onCancel = {
                    showPostCreator = false
                }
            )
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(post.author, style = MaterialTheme.typography.h6)
            Text(post.content, style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun PostCreationComponent(
    albums: List<String>,
    onPostCreated: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedAlbum by remember { mutableStateOf(albums.firstOrNull() ?: "") }
    var comment by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a Post", style = MaterialTheme.typography.h6)

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedAlbum.ifEmpty { "Select a song" })
                Icon(Icons.Filled.ArrowDropDown, "Drop Down")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                albums.forEach { album ->
                    DropdownMenuItem(onClick = {
                        selectedAlbum = album
                        expanded = false
                    }) {
                        Text(album)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Input text field with fixed size
        OutlinedTextField(
            value = comment,
            onValueChange = {
                if (it.length <= 200) comment = it
            },
            placeholder = { Text("Share your insights", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(130.dp), // fixed height for the text field
            textStyle = TextStyle(fontSize = 16.sp),
            maxLines = 5, // allow up to 5 lines of text
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black,
                disabledIndicatorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onPostCreated(selectedAlbum, comment) },
                enabled = comment.isNotBlank()
            ) {
                Text("Post")
            }

            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
