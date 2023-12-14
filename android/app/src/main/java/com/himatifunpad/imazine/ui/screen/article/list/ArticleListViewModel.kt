package com.himatifunpad.imazine.ui.screen.article.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.core.domain.repository.PostRepository
import com.himatifunpad.imazine.util.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
  post: PostRepository,
  state: SavedStateHandle
) : BaseViewModel() {
  val allPosts: Flow<PagingData<Post>> = post.getPosts(state.get<Int>("category_id") ?: 0)
    .cachedIn(viewModelScope)
}
