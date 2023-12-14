package com.himatifunpad.imazine.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.himatifunpad.imazine.core.data.local.DataStoreManager
import com.himatifunpad.imazine.core.data.remote.SafeApiRequest
import com.himatifunpad.imazine.core.data.remote.api.WpApiService
import com.himatifunpad.imazine.core.data.remote.json.CategoryJson
import com.himatifunpad.imazine.core.data.remote.json.PostErrorJson
import com.himatifunpad.imazine.core.data.remote.paging.PostPagingSource
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.core.domain.repository.PostRepository
import com.himatifunpad.imazine.util.ApiException
import com.himatifunpad.imazine.util.IMAZINE_CATEGORY
import com.himatifunpad.imazine.util.NoInternetException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
  private val api: WpApiService,
  private val prefs: DataStoreManager
) : PostRepository, SafeApiRequest() {
  override suspend fun getCategories(): Flow<Result<List<Category>>> = flow {
    try {
      val categories = apiRequest(
        { api.getCategories(page = 1, perPage = 50) },
        ::decodePostJson
      )
        .map(CategoryJson::toModel)
      emit(Result.success(categories))
    } catch (ex: ApiException) {
      emit(Result.failure(ex))
    } catch (ex: NoInternetException) {
      emit(Result.failure(ex))
    }
  }

  override suspend fun getPost(postId: Long): Flow<Result<Post>> =
    flow {
      try {
        val post = apiRequest(
          { api.getPost(postId = postId) },
          ::decodePostJson
        ).toModel()
        emit(Result.success(post))
      } catch (ex: ApiException) {
        emit(Result.failure(ex))
      } catch (ex: NoInternetException) {
        emit(Result.failure(ex))
      }
    }

  override suspend fun getLatestPost(): Flow<Result<Post>> =
    flow {
      try {
        val latestPosts = apiRequest(
          { api.getLatestPost(category = IMAZINE_CATEGORY) },
          ::decodePostJson
        ).firstOrNull() ?: throw ApiException("Post data is empty")

        val post = latestPosts.toModel()
        prefs.setLatestPostId(post.id)
        emit(Result.success(post))
      } catch (ex: ApiException) {
        emit(Result.failure(ex))
      } catch (ex: NoInternetException) {
        emit(Result.failure(ex))
      }
    }

  override fun getLatestPostId(): Flow<Long> = prefs.latestPostId

  override fun getPosts(categoryId: Int): Flow<PagingData<Post>> =
    Pager(
      PagingConfig(pageSize = 10, initialLoadSize = 10)
    ) {
      PostPagingSource(api, categoryId)
    }.flow

  private fun decodePostJson(str: String): String =
    Json.decodeFromString(PostErrorJson.serializer(), str).message
}
