package com.example.compose.thepostsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.thepostsapp.presentation.PostDetailScreen
import com.example.compose.thepostsapp.presentation.PostScreen
import com.example.compose.thepostsapp.ui.theme.ThePostsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThePostsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = "post_list"
                        ) {
                            composable("post_list") {
                                PostScreen(
                                    onNavigateToDetail = { postId ->
                                        navController.navigate("post_detail/$postId")
                                    }
                                )
                            }
                            composable(
                                route = "post_detail/{postId}",
                                arguments = listOf(
                                    navArgument("postId") {
                                        type = NavType.IntType
                                    }
                                )
                            ) {
                                PostDetailScreen(
                                    onNavigateBack = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
