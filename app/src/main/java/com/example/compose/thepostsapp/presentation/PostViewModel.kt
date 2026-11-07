package com.example.compose.thepostsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.use_case.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    val postsFlow: Flow<PagingData<Post>> = getPostsUseCase()
        .cachedIn(viewModelScope)
}
