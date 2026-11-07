package com.example.compose.thepostsapp.data.remote

import com.squareup.moshi.Json

data class PostDto(
    @Json(name = "id") val id: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "title") val title: String,
    @Json(name = "body") val body: String
)
