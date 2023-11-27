package com.example.commuzy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application()

//MainApplication: App 从打开到关闭(在后台的进程中kill process后)