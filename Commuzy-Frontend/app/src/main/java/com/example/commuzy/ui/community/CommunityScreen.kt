package com.example.commuzy.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.commuzy.R
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Post
import com.example.commuzy.ui.home.LoadingSection
import androidx.compose.ui.res.vectorResource

@Composable
fun CommunityScreen(viewModel: CommunityViewModel, onTap: (Album) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favoriteAlbums.collectAsState()
    CommunityScreenContent(uiState, favorites, viewModel, onTap)
}

@Composable
fun CommunityScreenContent(uiState: CommunityUiState, favorites: List<Album>, viewModel: CommunityViewModel, onTap: (Album) -> Unit) {
    var showPostCreator by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) }
    var selectedPostForComments by remember { mutableStateOf<Post?>(null) }

    Column {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item { CommunityScreenHeader(onAddPostClicked = { showPostCreator = true }) }
            if (uiState.isLoading) {
                item { LoadingSection(stringResource(id = R.string.screen_loading)) }
            } else {
                items(uiState.posts) { post ->
                    PostSection(
                        post = post,
                        viewModel = viewModel,
                        onTap = onTap,
                        onShowComments = { selectedPost ->
                            selectedPostForComments = selectedPost
                            showComments = true
                        }
                    )
                }
            }
        }
    }

    if (showPostCreator) {
        Dialog(onDismissRequest = { showPostCreator = false }) {
            PostCreationComponent(
                albums = favorites,
                onPostCreated = { comment, selectedAlbumId, selectedAlbumName ->
                    viewModel.createPost(comment, selectedAlbumId, selectedAlbumName) // Call ViewModel's createPost function to create post
                    showPostCreator = false
                },
                onCancel = {
                    showPostCreator = false
                }
            )
        }
    }

    selectedPostForComments?.let { selectedPost ->
        if (showComments) {
            CommentDialog(
                showComments = true,
                onHide = { showComments = false },
                post = selectedPost,
                viewModel = viewModel
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
fun PostCreationComponent(
    albums: List<Album>,
    onPostCreated: (String, Int, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedAlbum by remember { mutableStateOf(albums.firstOrNull() ?: Album.empty()) }
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
                Text(selectedAlbum.name.ifEmpty { "Select a album" })
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
                        Text(album.name)
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
                onClick = { onPostCreated(comment, selectedAlbum.id, selectedAlbum.name) },
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

@Composable
fun PostSection(
    post: Post,
    viewModel: CommunityViewModel,
    onTap: (Album) -> Unit,
    onShowComments: (Post) -> Unit
) {
    var album by remember { mutableStateOf<Album?>(null) }

    LaunchedEffect(post) {
        album = viewModel.findAlbumForPost(post)
    }

    if (album != null) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable { onTap(album!!) }
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = post.author,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.subtitle1
                )
                // AlbumRow display album info
                AlbumRow(album = album!!, onTap = onTap)

                // Comment and Delete icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_community_24),
                        contentDescription = "Comment",
                        modifier = Modifier.clickable { onShowComments(post) }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // 小的间距
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_thumb_up),
                            contentDescription = "Upvote",
                            modifier = Modifier.clickable { viewModel.upVotePost(post) }
                        )
                        Text("${post.upVotes}")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // 小的间距
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_thumb_down),
                            contentDescription = "Downvote",
                            modifier = Modifier.clickable { viewModel.downVotePost(post) }
                        )
                        Text("${post.downVotes}")
                    }

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.clickable { viewModel.deletePost(post) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AlbumRow(album: Album, onTap: (Album) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clickable {
                onTap(album)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = album.cover,
            contentDescription = null,
            modifier = Modifier
                .width(60.dp)
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .weight(1.0f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = album.name,
                style = MaterialTheme.typography.body2,
                color = Color.White,
            )

            Text(
                text = stringResource(id = R.string.album_info, album.artists, album.year),
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun CommentDialog(
    showComments: Boolean,
    onHide: () -> Unit,
    post: Post,
    viewModel: CommunityViewModel
) {
    if (showComments) {
        val comments by viewModel.getCommentsForPost(post.id).collectAsState(initial = emptyList())

        Dialog(onDismissRequest = { onHide() }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyColumn {
                        items(comments) { comment ->
                            Text(comment.content)
                        }
                    }

                    // Add new comment
                    val newCommentText = remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = newCommentText.value,
                        onValueChange = { newCommentText.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Add a comment") }
                    )
                    Button(
                        onClick = {
                            viewModel.addCommentToPost(post.id, newCommentText.value)
                            newCommentText.value = ""
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Post")
                    }
                }
            }
        }
    }
}

