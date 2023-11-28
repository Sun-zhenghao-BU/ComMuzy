package com.example.commuzy.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.commuzy.datamodel.Album

@Database(entities = [Album::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}

