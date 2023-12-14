package com.himatifunpad.imazine.core.domain.repository

import androidx.paging.PagingData
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
  suspend fun getCategories(): Flow<Result<List<Category>>>
  suspend fun getPost(postId: Long): Flow<Result<Post>>
  suspend fun getLatestPost(): Flow<Result<Post>>
  fun getLatestPostId(): Flow<Long>
  fun getPosts(categoryId: Int): Flow<PagingData<Post>>
}
