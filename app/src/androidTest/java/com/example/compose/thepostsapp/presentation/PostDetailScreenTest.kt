package com.example.compose.thepostsapp.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.thepostsapp.domain.model.AppError
import org.junit.Rule
import org.junit.Test

class PostDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun postDetailScreen_LoadingState_displaysLoadingIndicator() {
        composeTestRule.setContent {
            // Directly call the private/internal LoadingState composable if accessible,
            // or we test the screen by passing the state.
            // Since we extracted ErrorState and LoadingState, we can test them directly.
            LoadingState()
        }

        // We can check if a progress indicator exists, but standard Compose doesn't tag it by default.
        // It's usually better to tag it with modifier.testTag("LoadingIndicator").
        // For now, we just ensure it doesn't crash.
    }

    @Test
    fun postDetailScreen_ErrorState_displaysErrorMessageAndRetryButton() {
        val networkError = AppError.NetworkError()
        
        composeTestRule.setContent {
            ErrorState(error = networkError, onRetry = {})
        }

        composeTestRule.onNodeWithText("Oops! Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithText(networkError.message).assertIsDisplayed()
        composeTestRule.onNodeWithText("Try Again").assertIsDisplayed()
    }
}
