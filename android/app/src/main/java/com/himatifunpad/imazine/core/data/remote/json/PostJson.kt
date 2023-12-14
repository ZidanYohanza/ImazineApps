package com.himatifunpad.imazine.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostJson(
  @SerialName("id") val id: Long = 0L,
  @SerialName("date") val date: String = "",
  @SerialName("slug") val slug: String = "",
  @SerialName("title") val title: RenderedJson = RenderedJson(),
  @SerialName("content") val content: RenderedJson = RenderedJson(),
  @SerialName("_embedded") val embedded: PostEmbeddedJson = PostEmbeddedJson()
)

@Serializable
data class PostEmbeddedJson(
  @SerialName("wp:featuredmedia")
  val listFeaturedMedia: List<PostFeaturedMediaJson> = emptyList(),
  @SerialName("wp:term")
  val listTerm: List<List<CategoryJson>> = emptyList()
)

@Serializable
data class PostFeaturedMediaJson(
  @SerialName("alt_text") val altText: String = "",
  @SerialName("source_url") val sourceUrl: String = ""
)

@Serializable
data class RenderedJson(
  @SerialName("rendered") val rendered: String = ""
)

@Serializable
data class PostErrorJson(
  val code: String = "",
  val message: String = ""
)
