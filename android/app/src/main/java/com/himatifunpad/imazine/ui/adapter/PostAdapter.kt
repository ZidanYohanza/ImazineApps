package com.himatifunpad.imazine.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.databinding.ItemPostBinding
import com.himatifunpad.imazine.ui.adapter.PostAdapter.PostViewHolder
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.util.POST_DIFF

class PostAdapter(
  private val imageLoader: ImageLoader,
  private val onClick: (Post) -> Unit
) :
  PagingDataAdapter<Post, PostViewHolder>(POST_DIFF) {
  inner class PostViewHolder(private val binding: ItemPostBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Post?) {
      if (data == null) return
      with(binding) {
        root.setOnClickListener { onClick(data) }
        ivPostImg.apply {
          val imgData = ImageRequest.Builder(this.context)
            .data(data.cover)
            .target(this)
            .allowHardware(true)
            .build()
          imageLoader.enqueue(imgData)
        }
        tvCategory.text = data.category
        tvTitle.text = data.title
      }
    }
  }

  override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
    val data = getItem(position)
    holder.bind(data)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
    PostViewHolder(parent.viewBinding(ItemPostBinding::inflate))
}
