package com.example.compose.thepostsapp.presentation

import androidx.paging.PagingData
import com.example.compose.thepostsapp.MainDispatcherRule
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.use_case.GetPostsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val getPostsUseCase: GetPostsUseCase = mockk()

    @Test
    fun `postsFlow is initialized from getPostsUseCase`() {
        // Arrange
        val pagingData = PagingData.empty<Post>()
        every { getPostsUseCase() } returns flowOf(pagingData)

        // Act
        val viewModel = PostViewModel(getPostsUseCase)

        // Assert
        assertNotNull(viewModel.postsFlow)
    }
}
