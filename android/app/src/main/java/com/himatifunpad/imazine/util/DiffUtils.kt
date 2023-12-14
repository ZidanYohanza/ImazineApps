package com.himatifunpad.imazine.util

import androidx.recyclerview.widget.DiffUtil
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.core.domain.model.Post

val CATEGORY_DIFF = object : DiffUtil.ItemCallback<Category>() {
  override fun areItemsTheSame(
    oldItem: Category,
    newItem: Category
  ): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(
    oldItem: Category,
    newItem: Category
  ): Boolean = oldItem == newItem
}

val POST_DIFF = object : DiffUtil.ItemCallback<Post>() {
  override fun areItemsTheSame(
    oldItem: Post,
    newItem: Post
  ): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(
    oldItem: Post,
    newItem: Post
  ): Boolean = oldItem == newItem
}
