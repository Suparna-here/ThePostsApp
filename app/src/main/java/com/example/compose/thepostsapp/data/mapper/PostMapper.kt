package com.example.compose.thepostsapp.data.mapper

import com.example.compose.thepostsapp.data.remote.PostDto
import com.example.compose.thepostsapp.domain.model.Post

fun PostDto.toDomainPost(): Post {
    return Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}
