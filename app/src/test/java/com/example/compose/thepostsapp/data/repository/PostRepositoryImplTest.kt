package com.example.compose.thepostsapp.data.repository

import com.example.compose.thepostsapp.data.remote.APIService
import com.example.compose.thepostsapp.data.remote.PostDto
import com.example.compose.thepostsapp.domain.model.AppError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PostRepositoryImplTest {

    private lateinit var apiService: APIService
    private lateinit var repository: PostRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        apiService = mockk()
        // We inject the standard test dispatcher here
        repository = PostRepositoryImpl(apiService, testDispatcher)
    }

    @Test
    fun `getPosts returns Success when APIService succeeds`() {
        runTest(testDispatcher) {
            // Arrange
            val expectedDtos = listOf(
                PostDto(id = 1, userId = 1, title = "Test", body = "Body")
            )
            coEvery { apiService.getPosts() } returns expectedDtos

            // Act
            val result = repository.getPosts()

            // Assert
            assertTrue(result.isSuccess)
            val posts = result.getOrNull()
            assertEquals(1, posts?.size)
            assertEquals("Test", posts?.first()?.title)
        }
    }

    @Test
    fun `getPosts returns NetworkError when APIService throws IOException`() {
        runTest(testDispatcher) {
            // Arrange
            coEvery { apiService.getPosts() } throws IOException("No connection")

            // Act
            val result = repository.getPosts()

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.NetworkError)
        }
    }

    @Test
    fun `getPosts returns Unauthorized when APIService throws 401 HttpException`() {
        runTest(testDispatcher) {
            // Arrange
            val response = Response.error<List<PostDto>>(401, "".toResponseBody())
            coEvery { apiService.getPosts() } throws HttpException(response)

            // Act
            val result = repository.getPosts()

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.Unauthorized)
        }
    }

    @Test
    fun `getPosts returns ServerError when APIService throws 500 HttpException`() {
        runTest(testDispatcher) {
            // Arrange
            val response = Response.error<List<PostDto>>(500, "".toResponseBody())
            coEvery { apiService.getPosts() } throws HttpException(response)

            // Act
            val result = repository.getPosts()

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.ServerError)
        }
    }

}
