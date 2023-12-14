package com.himatifunpad.imazine.ui.screen.article.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.data.parcelize
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.databinding.FragmentArticleListBinding
import com.himatifunpad.imazine.ui.adapter.PostAdapter
import com.himatifunpad.imazine.ui.adapter.PostLoadStateAdapter
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.util.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ArticleListFragment : BaseFragment(R.layout.fragment_article_list) {
  private val binding by viewBinding<FragmentArticleListBinding>()
  private val viewModel by viewModels<ArticleListViewModel>()
  private val args by navArgs<ArticleListFragmentArgs>()

  private val toolbar get() = binding.toolbar
  private val rvPosts get() = binding.content.rvPosts
  private val swipeRefresh get() = binding.swipeRefresh

  private lateinit var postAdapter: PostAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.allPosts.collectLatest(postAdapter::submitData)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      postAdapter.loadStateFlow.collectLatest { loadState ->
        swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      postAdapter.loadStateFlow.map { it.refresh }
        .distinctUntilChanged()
        .collect {
          if (it is LoadState.NotLoading) {
            if (postAdapter.itemCount < 1) {
              showError("It's empty")
            }
          }
        }
    }
  }

  override fun setupView() {
    toolbar.title = args.categoryName
    toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    postAdapter = PostAdapter(
      imageLoader = imageLoader,
      onClick = { post ->
        moveToArticleDetail(post)
      }
    )
    rvPosts.adapter = postAdapter.withLoadStateHeaderAndFooter(
      header = PostLoadStateAdapter(postAdapter::retry),
      footer = PostLoadStateAdapter(postAdapter::retry)
    )
    swipeRefresh.setOnRefreshListener { refresh() }
  }

  private fun refresh() {
    hideError()
    postAdapter.refresh()
  }

  private fun showError(message: String) {
    binding.error.apply {
      root.isVisible = true
      lottieReaction.setAnimation("empty.json")
      tvDescription.text = message
      btnRetry.isVisible = false
    }
    binding.content.root.isVisible = false
  }

  private fun hideError() {
    binding.error.root.isVisible = false
    binding.content.root.isVisible = true
  }

  private fun moveToArticleDetail(post: Post) {
    findNavController().navigate(
      ArticleListFragmentDirections.actionArticleListToArticleDetail(post.parcelize())
    )
  }
}
