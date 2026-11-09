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
    fun `getPostById returns Success when APIService succeeds`() {
        runTest(testDispatcher) {
            // Arrange
            val expectedDto = PostDto(id = 1, userId = 1, title = "Test", body = "Body")
            coEvery { apiService.getPostById(1) } returns expectedDto

            // Act
            val result = repository.getPostById(1)

            // Assert
            assertTrue(result.isSuccess)
            val post = result.getOrNull()
            assertEquals("Test", post?.title)
        }
    }

    @Test
    fun `getPostById returns NetworkError when APIService throws IOException`() {
        runTest(testDispatcher) {
            // Arrange
            coEvery { apiService.getPostById(1) } throws IOException("No connection")

            // Act
            val result = repository.getPostById(1)

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.NetworkError)
        }
    }

    @Test
    fun `getPostById returns Unauthorized when APIService throws 401 HttpException`() {
        runTest(testDispatcher) {
            // Arrange
            val response = Response.error<PostDto>(401, "".toResponseBody())
            coEvery { apiService.getPostById(1) } throws HttpException(response)

            // Act
            val result = repository.getPostById(1)

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.Unauthorized)
        }
    }

    @Test
    fun `getPostById returns ServerError when APIService throws 500 HttpException`() {
        runTest(testDispatcher) {
            // Arrange
            val response = Response.error<PostDto>(500, "".toResponseBody())
            coEvery { apiService.getPostById(1) } throws HttpException(response)

            // Act
            val result = repository.getPostById(1)

            // Assert
            assertTrue(result.isFailure)
            val error = result.exceptionOrNull()
            assertTrue(error is AppError.ServerError)
        }
    }

}
