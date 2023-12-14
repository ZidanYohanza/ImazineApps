package com.himatifunpad.imazine.core.domain.model

data class Post(
  val id: Long,
  val date: String,
  val slug: String,
  val title: String,
  val content: String,
  val cover: String,
  val category: String
)
