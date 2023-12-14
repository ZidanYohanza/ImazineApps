package com.himatifunpad.imazine.core.domain.model

data class User(
  val npm: String,
  val nama: String
) {
  fun isEmpty() =
    npm.isEmpty() && nama.isEmpty()
}
