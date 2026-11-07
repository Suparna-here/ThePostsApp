package com.example.compose.thepostsapp.domain.use_case

import androidx.paging.PagingData
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getPosts()
    }
}
