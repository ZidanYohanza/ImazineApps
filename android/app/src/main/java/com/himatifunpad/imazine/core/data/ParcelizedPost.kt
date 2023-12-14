package com.himatifunpad.imazine.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @property id
 * @property date
 * @property slug
 * @property title
 * @property content
 * @property cover
 * @property category
 */
@Parcelize
data class ParcelizedPost(
  val id: Long,
  val date: String,
  val slug: String,
  val title: String,
  val content: String,
  val cover: String,
  val category: String
) : Parcelable
