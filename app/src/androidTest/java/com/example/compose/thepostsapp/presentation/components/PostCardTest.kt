package com.example.compose.thepostsapp.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.compose.thepostsapp.domain.model.Post
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PostCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun postCard_displaysTitleAndBody() {
        // Arrange
        val post = Post(
            id = 1,
            userId = 1,
            title = "Test Post Title",
            body = "This is the body of the test post."
        )

        // Act
        composeTestRule.setContent {
            PostCard(post = post, onClick = {})
        }

        // Assert
        composeTestRule.onNodeWithText("Test Post Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is the body of the test post.").assertIsDisplayed()
    }

    @Test
    fun postCard_invokesOnClickWhenClicked() {
        // Arrange
        val post = Post(
            id = 2,
            userId = 1,
            title = "Clickable Post",
            body = "Click me!"
        )
        var clicked = false

        // Act
        composeTestRule.setContent {
            PostCard(post = post, onClick = { clicked = true })
        }

        // Assert
        composeTestRule.onNodeWithText("Clickable Post").performClick()
        assertTrue(clicked)
    }
}
