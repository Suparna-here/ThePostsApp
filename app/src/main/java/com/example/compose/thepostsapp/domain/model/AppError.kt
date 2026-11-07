package com.example.compose.thepostsapp.domain.model

sealed class AppError(override val message: String) : Throwable(message) {
    class NetworkError : AppError("No internet connection. Please check your network.")
    class ServerError : AppError("Server is unreachable. Please try again later.")
    class Unauthorized : AppError("Session expired. Please log in again.")
    class NotFound : AppError("The requested resource was not found.")
    data class UnknownError(val error: Throwable) : AppError(error.message ?: "An unexpected error occurred.")
}
