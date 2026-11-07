# The Posts App 📱

A modern Android application built to demonstrate the practical implementation of Clean Architecture, MVVM design pattern, and Jetpack Compose. This app fetches and displays posts using a public REST API, showcasing industry best practices in state management, dependency injection, and network security.

## 🌟 Features

*   **Jetpack Compose UI**: Fully declarative UI with Material 3 design guidelines.
*   **Pagination (Paging 3)**: Efficiently loads data in chunks as the user scrolls, conserving memory and network bandwidth.
*   **Swipe-to-Refresh**: Integrated native pull-to-refresh mechanics using Material 3 `PullToRefreshBox`.
*   **Robust Error Handling**: Domain-specific error mapping (e.g., `NetworkError`, `ServerError`) mapped natively from HTTP responses.
*   **State Management**: Unidirectional Data Flow (UDF) using Kotlin `StateFlow` and sealed UI states.
*   **Dependency Injection**: Modular and testable architecture using **Dagger-Hilt**.
*   **Secure Networking**: Configured with strict HTTPS enforcement, cleartext banning, and debug-only HTTP logging using OkHttp interceptors.

## 🛠️ Tech Stack & Libraries

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
*   **Networking:** 
    *   [Retrofit2](https://square.github.io/retrofit/) - Type-safe HTTP client.
    *   [OkHttp3](https://square.github.io/okhttp/) - HTTP client with `HttpLoggingInterceptor`.
*   **JSON Serialization:** [Moshi](https://github.com/square/moshi) (with Kotlin codegen via KSP).
*   **Asynchrony:** [Kotlin Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html)
*   **Data Paging:** [Jetpack Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
*   **Testing:** 
    *   Unit Testing: JUnit 4, [MockK](https://mockk.io/), and `kotlinx-coroutines-test`.
    *   UI Testing: Jetpack Compose Testing (`ui-test-junit4`, `ui-test-manifest`).

## 🏗️ Architecture

This project strictly adheres to **Clean Architecture** principles, separating concerns into three distinct layers to ensure testability and scalability:

1.  **Domain Layer** (Inner-most): Contains the core business logic, domain models (`Post`, `AppError`), repository interfaces, and Use Cases (`GetPostsUseCase`). This layer has zero dependencies on Android frameworks or external libraries.
2.  **Data Layer**: Contains data sources (Retrofit `APIService`), DTOs, mappers, and repository implementations (`PostRepositoryImpl`). It acts as the single source of truth for data and handles thread switching via injected `CoroutineDispatcher`s.
3.  **Presentation Layer** (Outer-most): Contains the Jetpack Compose UI (`PostScreen`, `PostDetailScreen`) and ViewModels. It reacts to states emitted by the Domain layer and handles user interactions.

## 🛡️ Security Best Practices Implemented

*   **Network Security Config**: Includes a custom `network_security_config.xml` that enforces HTTPS and restricts trust anchors.
*   **Safe Debug Logging**: The `HttpLoggingInterceptor` is strictly wrapped in a `BuildConfig.DEBUG` check, ensuring sensitive network payloads are never leaked in the release build's Logcat.

## 🧪 Testing

The project implements a comprehensive testing strategy across all layers:
*   **Unit Tests (Domain & Data Layer)**: Tests the repository logic, mapping, and error handling using JUnit 4, MockK, and Coroutines Test (`kotlinx-coroutines-test`).
*   **UI Tests (Presentation Layer)**: Tests Jetpack Compose components in isolation using `composeTestRule` to verify data binding and user interactions (like clicks and error states).

**To run the tests:**
*   Unit Tests: `./gradlew testDebugUnitTest`
*   UI Tests: `./gradlew connectedDebugAndroidTest`

## 🚀 Getting Started

### Prerequisites
*   [Android Studio (latest stable version)](https://developer.android.com/studio)
*   JDK 17 or higher.

### Installation
1.  Clone this repository:
    ```bash
    git clone https://github.com/Suparna-here/ThePostsApp.git
    ```
2.  Open the project in Android Studio.
3.  Sync the Gradle project to download all necessary dependencies.
4.  Run the app on an Emulator or a physical device (Requires Android API 24+).

---
*Developed with ❤️ using modern Android practices.*

## Screen Shots
### Phone
## Posts Listing
<img src="https://github.com/Suparna-here/ThePostsApp/tree/main/ScreenShots/Phone/PostsListing.png" width="40%" />

## Post In Detail
<img src="https://github.com/Suparna-here/ThePostsApp/tree/main/ScreenShots/Phone/PostInDetail.png" width="40%" />
