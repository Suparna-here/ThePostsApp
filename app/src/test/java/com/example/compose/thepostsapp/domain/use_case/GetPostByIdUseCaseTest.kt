package com.example.compose.thepostsapp.domain.use_case

import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.repository.PostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPostByIdUseCaseTest {

    private lateinit var getPostByIdUseCase: GetPostByIdUseCase
    private val postRepository: PostRepository = mockk()

    @Before
    fun setUp() {
        getPostByIdUseCase = GetPostByIdUseCase(postRepository)
    }

    @Test
    fun `invoke should return post when repository returns success`() = runTest {
        // Arrange
        val postId = 1
        val expectedPost = Post(id = postId, userId = 1, title = "Title", body = "Body")
        coEvery { postRepository.getPostById(postId) } returns Result.success(expectedPost)

        // Act
        val result = getPostByIdUseCase(postId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(expectedPost, result.getOrNull())
        coVerify(exactly = 1) { postRepository.getPostById(postId) }
    }

    @Test
    fun `invoke should return error when repository returns failure`() = runTest {
        // Arrange
        val postId = 1
        val expectedError = AppError.NetworkError()
        coEvery { postRepository.getPostById(postId) } returns Result.failure(expectedError)

        // Act
        val result = getPostByIdUseCase(postId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
        coVerify(exactly = 1) { postRepository.getPostById(postId) }
    }
}
