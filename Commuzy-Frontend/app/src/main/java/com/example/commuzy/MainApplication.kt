package com.example.commuzy

import android.app.Application
import androidx.room.Room
import com.example.commuzy.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "commuzy_db").build()
    }
}
