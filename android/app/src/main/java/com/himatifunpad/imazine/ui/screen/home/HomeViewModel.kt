package com.himatifunpad.imazine.ui.screen.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.core.domain.repository.PostRepository
import com.himatifunpad.imazine.ui.screen.home.HomeViewModel.HomeEvent.UpdateLatestPost
import com.himatifunpad.imazine.util.IMAZINE_CATEGORY
import com.himatifunpad.imazine.util.base.BaseEvent
import com.himatifunpad.imazine.util.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val post: PostRepository
) : BaseViewModel() {
  sealed class HomeEvent : BaseEvent() {
    data class UpdateLatestPost(val post: Post) : HomeEvent()
  }

  private val _categories = MutableStateFlow<List<Category>>(emptyList())
  val categories get() = _categories
  val allPosts: Flow<PagingData<Post>> = post.getPosts(IMAZINE_CATEGORY)
    .cachedIn(viewModelScope)

  private fun fetchCategories() {
    viewModelScope.launch {
      post.getCategories()
        .catch {
          setErrorMessage(it.message)
        }
        .collect { result ->
          if (result.isSuccess) _categories.emit(result.getOrThrow())
          else if (result.isFailure) setErrorMessage(result.exceptionOrNull()?.message)
        }
    }
  }

  private fun fetchLatestPost() {
    viewModelScope.launch {
      post.getLatestPost()
        .catch {
          setErrorMessage(it.message)
        }
        .collect { result ->
          if (result.isSuccess) sendNewEvent(UpdateLatestPost(result.getOrThrow()))
          else if (result.isFailure) setErrorMessage(result.exceptionOrNull()?.message)
        }
    }
  }

  fun onRefresh() {
    fetchCategories()
    fetchLatestPost()
  }
}
