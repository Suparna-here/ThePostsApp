package com.example.compose.thepostsapp.presentation

import com.example.compose.thepostsapp.MainDispatcherRule
import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.use_case.GetPostsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPostsUseCase: GetPostsUseCase = mockk()

    @Test
    fun `initial state is Loading and changes to Success when fetch is successful`() = runTest(testDispatcher) {
        // Arrange
        val posts = listOf(
            Post(id = 1, userId = 1, title = "Title 1", body = "Body 1"),
            Post(id = 2, userId = 1, title = "Title 2", body = "Body 2")
        )
        
        coEvery { getPostsUseCase() } returns Result.success(posts)

        // Act
        val viewModel = PostViewModel(getPostsUseCase, testDispatcher)

        // Assert initial state
        assertTrue(viewModel.uiState.value is PostUiState.Loading)
        
        // Execute pending coroutines
        advanceUntilIdle()

        // Assert final state
        val uiState = viewModel.uiState.value
        assertTrue(uiState is PostUiState.Success)
        assertEquals(posts, (uiState as PostUiState.Success).posts)
    }

    @Test
    fun `state changes to Error when fetch fails with AppError`() = runTest(testDispatcher) {
        // Arrange
        val error = AppError.NetworkError()
        
        coEvery { getPostsUseCase() } returns Result.failure(error)

        // Act
        val viewModel = PostViewModel(getPostsUseCase, testDispatcher)

        // Assert initial state
        assertTrue(viewModel.uiState.value is PostUiState.Loading)

        // Execute pending coroutines
        advanceUntilIdle()

        // Assert final state
        val uiState = viewModel.uiState.value
        assertTrue(uiState is PostUiState.Error)
        assertEquals(error, (uiState as PostUiState.Error).error)
    }

    @Test
    fun `state changes to Error when fetch fails with generic Exception`() = runTest(testDispatcher) {
        // Arrange
        val exception = RuntimeException("Something went wrong")
        
        coEvery { getPostsUseCase() } returns Result.failure(exception)

        // Act
        val viewModel = PostViewModel(getPostsUseCase, testDispatcher)

        // Execute pending coroutines
        advanceUntilIdle()

        // Assert final state
        val uiState = viewModel.uiState.value
        assertTrue(uiState is PostUiState.Error)
        
        val appError = (uiState as PostUiState.Error).error
        assertTrue(appError is AppError.UnknownError)
        assertEquals(exception.message, appError.message)
    }
}
