package com.himatifunpad.imazine.ui.screen.article.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.core.domain.repository.PostRepository
import com.himatifunpad.imazine.ui.screen.article.detail.ArticleDetailViewModel.ArticleDetailEvent.ArticleLoaded
import com.himatifunpad.imazine.util.base.BaseEvent
import com.himatifunpad.imazine.util.base.BaseEvent.ShowErrorMessage
import com.himatifunpad.imazine.util.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
  private val post: PostRepository,
  val state: SavedStateHandle
) : BaseViewModel() {
  private val postId = state.get<Long>("post_id") ?: 0L

  sealed class ArticleDetailEvent : BaseEvent() {
    data class ArticleLoaded(val post: Post) : ArticleDetailEvent()
  }

  init {
    viewModelScope.launch {
      post.getPost(postId)
        .catch { ShowErrorMessage(it.message ?: "") }
        .collect { result ->
          if (result.isSuccess) sendNewEvent(ArticleLoaded(result.getOrThrow()))
          else if (result.isFailure) setErrorMessage(result.exceptionOrNull()?.message)
        }
    }
  }
}
