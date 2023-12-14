package com.himatifunpad.imazine.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryJson(
  @SerialName("id") val id: Int = 0,
  @SerialName("name") val name: String = "",
  @SerialName("slug") val slug: String = ""
)
