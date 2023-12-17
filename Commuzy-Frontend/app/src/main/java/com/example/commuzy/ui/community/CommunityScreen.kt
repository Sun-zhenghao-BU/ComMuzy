package com.example.commuzy.ui.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.commuzy.R


@Composable
fun CommunityScreen(viewModel: CommunityViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    CommunityScreenContent(uiState)
}

@Composable
fun CommunityScreenContent(uiState: CommunityUiState) {
    LazyColumn (
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            CommunityScreenHeader()
        }
    }
}

@Composable
fun CommunityScreenHeader() {
    Column {
        Text(
            stringResource(id = R.string.menu_community),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PostCreationComponent(onPostCreated: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Share your insights now") }
        )
        Button(
            onClick = { onPostCreated(text) },
            enabled = text.isNotBlank()
        ) {
            Text("Post")
        }
    }
}