package com.example.compose.thepostsapp.domain.use_case

import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.repository.PostRepository
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(id: Int): Result<Post> {
        return repository.getPostById(id)
    }
}
