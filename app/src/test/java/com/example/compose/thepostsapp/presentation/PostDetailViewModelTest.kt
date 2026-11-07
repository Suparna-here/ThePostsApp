package com.example.compose.thepostsapp.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.compose.thepostsapp.MainDispatcherRule
import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.use_case.GetPostByIdUseCase
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
class PostDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPostByIdUseCase: GetPostByIdUseCase = mockk()

    @Test
    fun `initial state is Loading and changes to Success when fetch is successful`() = runTest(testDispatcher) {
        // Arrange
        val postId = 1
        val savedStateHandle = SavedStateHandle(mapOf("postId" to postId))
        val post = Post(id = postId, userId = 1, title = "Title", body = "Body")
        
        coEvery { getPostByIdUseCase(postId) } returns Result.success(post)

        // Act
        val viewModel = PostDetailViewModel(getPostByIdUseCase, savedStateHandle)

        // Assert initial state
        assertTrue(viewModel.uiState.value is PostDetailUiState.Loading)
        
        // Execute pending coroutines
        advanceUntilIdle()

        // Assert final state
        val uiState = viewModel.uiState.value
        assertTrue(uiState is PostDetailUiState.Success)
        assertEquals(post, (uiState as PostDetailUiState.Success).post)
    }

    @Test
    fun `state changes to Error when fetch fails`() = runTest(testDispatcher) {
        // Arrange
        val postId = 1
        val savedStateHandle = SavedStateHandle(mapOf("postId" to postId))
        val error = AppError.NetworkError()
        
        coEvery { getPostByIdUseCase(postId) } returns Result.failure(error)

        // Act
        val viewModel = PostDetailViewModel(getPostByIdUseCase, savedStateHandle)

        // Assert initial state
        assertTrue(viewModel.uiState.value is PostDetailUiState.Loading)

        // Execute pending coroutines
        advanceUntilIdle()

        // Assert final state
        val uiState = viewModel.uiState.value
        assertTrue(uiState is PostDetailUiState.Error)
        assertEquals(error, (uiState as PostDetailUiState.Error).error)
    }
}
