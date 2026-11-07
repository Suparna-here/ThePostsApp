package com.example.compose.thepostsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.compose.thepostsapp.data.mapper.toDomainPost
import com.example.compose.thepostsapp.data.remote.APIService
import com.example.compose.thepostsapp.di.IoDispatcher
import com.example.compose.thepostsapp.domain.model.AppError
import com.example.compose.thepostsapp.domain.model.Post
import com.example.compose.thepostsapp.domain.repository.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PostRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PostRepository {
    override fun getPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(apiService) }
        ).flow
    }

    override suspend fun getPostById(id: Int): Result<Post> = withContext(ioDispatcher) {
        try {
            val response = apiService.getPostById(id)
            Result.success(response.toDomainPost())
        } catch (_: IOException) {
            Result.failure(AppError.NetworkError())
        } catch (e: HttpException) {
            val error = when (e.code()) {
                401 -> AppError.Unauthorized()
                404 -> AppError.NotFound()
                in 500..599 -> AppError.ServerError()
                else -> AppError.UnknownError(e)
            }
            Result.failure(error)
        } catch (e: CancellationException) {
            throw e // Re-throw cancellation to let coroutines handle it
        } catch (e: Exception) {
            Result.failure(AppError.UnknownError(e))
        }
    }
}
