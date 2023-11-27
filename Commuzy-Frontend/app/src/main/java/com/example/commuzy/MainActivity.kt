package com.example.commuzy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.commuzy.ui.theme.CommuzyTheme

// customized extend AppCompatActivity
class MainActivity : AppCompatActivity() {

    private val TAG = "lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "We are at onCreate()")
        setContentView(R.layout.activity_main)
    }
}


@Composable
fun AlbumCover() {
    Column {
        Box(modifier = Modifier.size(160.dp)) {
            AsyncImage(
                model = "https://upload.wikimedia.org/wikipedia/en/d/d1/Stillfantasy.jpg",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "Still Fantasy",
                color = Color.White,
                modifier = Modifier.padding(8.dp).align(Alignment.BottomStart)
            )
        }
        Text(
            text = "Jay Chou",
            color = Color.White,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 4.dp, start = 8.dp)
        )
    }
}

 @Composable
 fun StatelessHelloContent(name: String, onNameChange: (String) -> Unit) {
     Column(modifier = Modifier.padding(16.dp)) {
         if (name.isNotEmpty()) {
             Text(
                 text = "Hello, $name!",
                 modifier = Modifier.padding(bottom = 8.dp),
                 style = MaterialTheme.typography.body2
             )
         }
         OutlinedTextField(
             value = name,
             onValueChange = onNameChange,
             label = { Text("Name") }
         )
     }
 }

@Preview(showBackground = true, widthDp = 412, heightDp = 732)
@Composable
fun PreviewHelloContent() {
    CommuzyTheme {
        Surface {
            AlbumCover()
        }
    }
}