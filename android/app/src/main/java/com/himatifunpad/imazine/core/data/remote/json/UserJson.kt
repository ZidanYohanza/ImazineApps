package com.himatifunpad.imazine.core.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class UserJson(
  val npm: String = "",
  val nama: String = ""
)
