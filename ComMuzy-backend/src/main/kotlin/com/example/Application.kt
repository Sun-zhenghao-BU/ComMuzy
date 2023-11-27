package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class Song (
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)

@Serializable
data class Playlist(
    val id: Long,
    val songs: List<Song>
)

fun main() {
    // http engine
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    // TODO: adding the routing configuration here
    routing {
        // endpoint
        get("/") {
            call.respondText("Hello World!")
        }

        get("/feed") {
            val jsonString = this::class.java.classLoader.getResource("feed.json")?.readText()
            val json = Json.parseToJsonElement(jsonString ?: "")
            call.respondText(json.toString(), ContentType.Application.Json)
        }

        get("/playlists") {
            val jsonString = this::class.java.classLoader.getResource("playlists.json")?.readText()
            val json = Json.parseToJsonElement(jsonString ?: "")
            call.respondText(json.toString(), ContentType.Application.Json)
        }

        get("/playlist/{id}") {
            val jsonString = this::class.java.classLoader.getResource("playlists.json")?.readText()
            val playlists: List<Playlist> = Json.decodeFromString(ListSerializer(Playlist.serializer()), jsonString ?: "")
            val id = call.parameters["id"]
            val playlist = playlists.firstOrNull { it.id.toString() == id }
            call.respondNullable(playlist)
        }

        static("/") {
            staticBasePackage = "static"
            static("songs") {
                resources("songs")
            }
        }
    }
}
