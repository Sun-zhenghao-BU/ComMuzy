package com.example.commuzy.datamodel

import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("section_title")
    val sectionTitle: String,
    val albums: List<Album>
)

