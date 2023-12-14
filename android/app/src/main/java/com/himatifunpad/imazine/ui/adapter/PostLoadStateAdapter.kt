package com.himatifunpad.imazine.ui.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.himatifunpad.imazine.databinding.ItemLoadStateBinding
import com.himatifunpad.imazine.ui.adapter.PostLoadStateAdapter.LoadStateViewHolder
import com.himatifunpad.imazine.ui.ext.viewBinding

class PostLoadStateAdapter(
  private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {
  inner class LoadStateViewHolder(private val binding: ItemLoadStateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(loadState: LoadState) {
      with(binding) {
        btnRetry.setOnClickListener {
          retry
        }
        if (loadState is LoadState.Error) {
          lblErrorMessage.text = loadState.error.localizedMessage
        }
        progressBar.isVisible = loadState is LoadState.Loading
        btnRetry.isVisible = loadState is LoadState.Error
        lblErrorMessage.isVisible = loadState is LoadState.Error
      }
    }
  }

  override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
    holder.bind(loadState)

  override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
    LoadStateViewHolder(parent.viewBinding(ItemLoadStateBinding::inflate))
}
