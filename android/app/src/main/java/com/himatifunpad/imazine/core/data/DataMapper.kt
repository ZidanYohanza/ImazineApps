package com.himatifunpad.imazine.core.data

import com.himatifunpad.imazine.core.data.remote.json.CategoryJson
import com.himatifunpad.imazine.core.data.remote.json.PostJson
import com.himatifunpad.imazine.core.data.remote.json.UserJson
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.core.domain.model.User

fun UserJson.toModel() =
  User(
    nama = this.nama,
    npm = this.npm
  )

fun PostJson.toModel() =
  Post(
    id = id,
    date = date,
    slug = slug,
    title = title.rendered,
    content = content.rendered,
    cover = if (embedded.listFeaturedMedia.isNotEmpty()) {
      embedded.listFeaturedMedia.first().sourceUrl
    } else {
      ""
    },
    category = if (embedded.listTerm[0].isNotEmpty()) {
      embedded.listTerm[0].first().name
    } else {
      "Artikel"
    }
  )

fun CategoryJson.toModel() =
  Category(
    id = id,
    name = name,
    slug = slug
  )

fun ParcelizedPost.toModel() =
  Post(
    id = id,
    date = date,
    slug = slug,
    title = title,
    content = content,
    cover = cover,
    category = category
  )

fun Post.parcelize() =
  ParcelizedPost(
    id = id,
    date = date,
    slug = slug,
    title = title,
    content = content,
    cover = cover,
    category = category
  )
