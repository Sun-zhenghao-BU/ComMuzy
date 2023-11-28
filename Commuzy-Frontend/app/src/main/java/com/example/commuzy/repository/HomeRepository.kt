package com.example.commuzy.repository

import com.example.commuzy.datamodel.Section
import com.example.commuzy.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getHomeSections(): List<Section> =
        withContext(Dispatchers.IO) {
            delay(3000)
            val call = networkApi.getHomeFeed()
            val response = call.execute()

            response.body() ?: listOf()
        }
}