package com.example.compose.thepostsapp.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.use_case.GetPostByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val post: Post) : PostDetailUiState
    data class Error(val error: AppError) : PostDetailUiState
}

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val postId: Int = checkNotNull(savedStateHandle["postId"])

    init {
        fetchPostDetails()
    }

    fun fetchPostDetails() {
        viewModelScope.launch {
            _uiState.update { PostDetailUiState.Loading }
            getPostByIdUseCase(postId)
                .onSuccess { post ->
                    _uiState.update { PostDetailUiState.Success(post) }
                }
                .onFailure { error ->
                    _uiState.update { PostDetailUiState.Error(error as AppError) }
                }
        }
    }
}
