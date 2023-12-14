package com.himatifunpad.imazine.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthJson(
  val status: String = "",
  val user: UserJson? = null,
  @SerialName("Token") val token: String = ""
)
