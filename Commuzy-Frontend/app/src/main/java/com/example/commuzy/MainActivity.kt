package com.example.commuzy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
//import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import coil.compose.AsyncImage
import com.example.commuzy.database.DatabaseDao
import com.example.commuzy.datamodel.Album
import com.example.commuzy.network.NetworkApi
import com.example.commuzy.network.NetworkModule
import com.example.commuzy.player.PlayerBar
import com.example.commuzy.player.PlayerViewModel

import com.example.commuzy.ui.theme.CommuzyTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth

// customized extend AppCompatActivity
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "lifecycle"
    @Inject
    lateinit var api: NetworkApi
    @Inject
    lateinit var databaseDao: DatabaseDao
    private val playerViewModel: PlayerViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "We are at onCreate()")
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navHostFragment =supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)

        NavigationUI.setupWithNavController(navView, navController)

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // 用户未登录，跳转到登录界面
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        // https://stackoverflow.com/questions/70703505/navigationui-not-working-correctly-with-bottom-navigation-view-implementation
        navView.setOnItemSelectedListener{
            NavigationUI.onNavDestinationSelected(it, navController)
            navController.popBackStack(it.itemId, inclusive = false)
            true
        }
        val playerBar = findViewById<ComposeView>(R.id.player_bar)
        playerBar.apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    PlayerBar(
                        playerViewModel
                    )
                }
            }
        }


//      Test retrofit
        GlobalScope.launch(Dispatchers.IO) {
//            val api = NetworkApiImpl()
//            val retrofit = NetworkModule.provideRetrofit()
//            val api = retrofit.create(NetworkApi::class.java)

            val response = api.getHomeFeed().execute().body()
            Log.d("Network", response.toString())
        }
        // remember it runs everytime you start the app
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val album = Album(
                    id = 1,
                    name =  "Hexagonal",
                    year = "2008",
                    cover = "https://upload.wikimedia.org/wikipedia/en/6/6d/Leessang-Hexagonal_%28cover%29.jpg",
                    artists = "Lesssang",
                    description = "Leessang (Korean: 리쌍) was a South Korean hip hop duo, composed of Kang Hee-gun (Gary or Garie) and Gil Seong-joon (Gil)"
                )
                databaseDao.favoriteAlbum(album)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }


}

@Composable
private fun LoadingSection(text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
    }
}

@Composable
fun AlbumCover() {
    Column() {
        Box(modifier = Modifier.size(160.dp)) {
            AsyncImage(
                model = "https://upload.wikimedia.org/wikipedia/en/d/d1/Stillfantasy.jpg",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Text( // ctrl + J
                text = "Still Fantasy",
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp, start = 2.dp).align(Alignment.BottomStart)
            )
        }
        Text(
            text = "jay Chu",
            modifier = Modifier.padding(top = 4.dp),
            style=MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            color = Color.White)
    }
}

@Preview(showBackground = true, name = "Preview LoadingSection")
@Composable
fun DefaultPreview() {
    CommuzyTheme {
        Surface {
            AlbumCover()
        }
    }
}

