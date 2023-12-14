package com.himatifunpad.imazine.ui.screen.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import coil.request.ImageRequest
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.data.parcelize
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.databinding.FragmentHomeBinding
import com.himatifunpad.imazine.ui.adapter.CategoryAdapter
import com.himatifunpad.imazine.ui.adapter.PostAdapter
import com.himatifunpad.imazine.ui.adapter.PostLoadStateAdapter
import com.himatifunpad.imazine.ui.ext.snackbar
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.ui.screen.home.HomeViewModel.HomeEvent
import com.himatifunpad.imazine.util.base.BaseEvent.ShowErrorMessage
import com.himatifunpad.imazine.util.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment(R.layout.fragment_home) {
  private val binding by viewBinding<FragmentHomeBinding>()
  private val viewModel by viewModels<HomeViewModel>()

  private val toolbar get() = binding.toolbar
  private val rvCategory get() = binding.content.rvCategory
  private val rvAllPosts get() = binding.content.allPosts.rvPosts
  private val latestPost get() = binding.content.latestPost
  private val swipeRefresh get() = binding.swipeRefresh

  private lateinit var postAdapter: PostAdapter
  private lateinit var categoryAdapter: CategoryAdapter

  override fun onStart() {
    super.onStart()
    eventJob = viewModel.events
      .onEach { event ->
        when (event) {
          is HomeEvent.UpdateLatestPost -> {
            toggleLoading(false)
            updateLatestPost(event.post)
          }
          is ShowErrorMessage -> {
            toggleLoading(false)
            showError(event.message, "alert.json")
            snackbar("Error : ${event.message}")
          }
        }
      }.launchIn(viewLifecycleOwner.lifecycleScope)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupView()
    refresh()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.categories.collectLatest { list ->
        categoryAdapter.submitList(list)
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.allPosts.collectLatest(postAdapter::submitData)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      postAdapter.loadStateFlow.map { it.refresh }
        .distinctUntilChanged()
        .collect {
          if (it is LoadState.NotLoading) {
            if (postAdapter.itemCount < 1) {
              showError("It's empty", "empty.json")
            }
          }
        }
    }
  }

  @SuppressLint("RestrictedAPI")
  override fun setupView() {
    (toolbar.menu as MenuBuilder).setOptionalIconsVisible(true)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.item_settings -> {
          findNavController().navigate(
            HomeFragmentDirections.actionHomeToSettings()
          )
        }
        R.id.item_about -> {
          findNavController().navigate(
            HomeFragmentDirections.actionHomeToAbout()
          )
        }
      }
      true
    }
    swipeRefresh.setOnRefreshListener { refresh() }

    categoryAdapter = CategoryAdapter(
      onClick = { id, name ->
        findNavController().navigate(
          HomeFragmentDirections.actionHomeToArticleList(id, name)
        )
      }
    )
    rvCategory.adapter = categoryAdapter

    postAdapter = PostAdapter(
      imageLoader = imageLoader,
      onClick = { post ->
        moveToArticleDetail(post)
      }
    )
    rvAllPosts.adapter = postAdapter.withLoadStateHeaderAndFooter(
      header = PostLoadStateAdapter(postAdapter::retry),
      footer = PostLoadStateAdapter(postAdapter::retry)
    )
    binding.error.btnRetry.setOnClickListener { refresh() }
  }

  private fun refresh() {
    // making sure error layout is hidden
    binding.error.root.isVisible = false
    binding.content.root.isVisible = true

    // then refresh the content
    toggleLoading(true)
    viewModel.onRefresh()
    postAdapter.refresh()
  }

  private fun toggleLoading(show: Boolean) {
    latestPost.content.isVisible = !show
    latestPost.progressBar.isVisible = show
    swipeRefresh.isRefreshing = show
  }

  private fun showError(message: String, lottieFileName: String) {
    binding.error.apply {
      root.isVisible = true
      tvDescription.text = message
      lottieReaction.setAnimation(lottieFileName)
    }
    binding.content.root.isVisible = false
  }

  private fun updateLatestPost(post: Post) {
    with(latestPost) {
      this.root.setOnClickListener { moveToArticleDetail(post) }
      ivFeaturedImg.apply {
        val imgData = ImageRequest.Builder(this.context)
          .data(post.cover)
          .target(this)
          .allowHardware(true)
          .build()
        imageLoader.enqueue(imgData)
      }
      tvCategory.text = post.category
      tvTitle.text = post.title
    }
  }

  private fun moveToArticleDetail(post: Post) {
    findNavController().navigate(
      HomeFragmentDirections.actionHomeToArticleDetail(post.parcelize())
    )
  }
}
