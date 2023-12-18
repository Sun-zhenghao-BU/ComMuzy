package com.example.commuzy.database

import androidx.room.TypeConverter
import com.example.commuzy.datamodel.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCommentsList(comments: List<Comment>): String {
        return Gson().toJson(comments)
    }

    @TypeConverter
    fun toCommentsList(commentsString: String): List<Comment> {
        val listType = object : TypeToken<List<Comment>>() {}.type
        return Gson().fromJson(commentsString, listType)
    }
}
