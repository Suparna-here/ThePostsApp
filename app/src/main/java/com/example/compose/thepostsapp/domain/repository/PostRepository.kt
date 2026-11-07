package com.example.compose.thepostsapp.domain.repository

import androidx.paging.PagingData
import com.example.compose.thepostsapp.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
    suspend fun getPostById(id: Int): Result<Post>
}
