package com.example.commuzy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.commuzy.datamodel.Album
import com.example.commuzy.datamodel.Comment
import com.example.commuzy.datamodel.Post

@Database(entities = [Album::class, Post::class, Comment::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}

