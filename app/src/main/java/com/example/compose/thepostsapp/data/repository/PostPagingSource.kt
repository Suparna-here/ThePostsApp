package com.example.compose.thepostsapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.compose.thepostsapp.data.mapper.toDomainPost
import com.example.compose.thepostsapp.data.remote.APIService
import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import retrofit2.HttpException
import java.io.IOException

class PostPagingSource(
    private val apiService: APIService
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 1
        return try {
            val limit = params.loadSize
            val response = apiService.getPosts(page = page, limit = limit)
            val posts = response.map { it.toDomainPost() }

            LoadResult.Page(
                data = posts,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (posts.isEmpty()) null else page + 1
            )
        } catch (_: IOException) {
            LoadResult.Error(AppError.NetworkError())
        } catch (e: HttpException) {
            val error = when (e.code()) {
                401 -> AppError.Unauthorized()
                404 -> AppError.NotFound()
                in 500..599 -> AppError.ServerError()
                else -> AppError.UnknownError(e)
            }
            LoadResult.Error(error)
        } catch (e: Exception) {
            LoadResult.Error(AppError.UnknownError(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
