package com.himatifunpad.imazine.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat

object NotificationUtil {
  object ChannelName {
    const val NEW_POST = "New post"
  }

  data class NotificationData(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val channel: String
  )

  data class NotificationChannelData(
    @StringRes val id: Int,
    val name: String,
    val description: String,
    val importance: Int,
    val showBadge: Boolean
  )

  fun createNotificationChannel(
    context: Context,
    channelData: NotificationChannelData
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channelId = context.getString(channelData.id)
      val channel = NotificationChannel(channelId, channelData.name, channelData.importance)
      channel.description = channelData.description
      channel.setShowBadge(channelData.showBadge)

      NotificationManagerCompat.from(context)
        .createNotificationChannel(channel)
    }
  }

  fun showNotification(context: Context, id: Int, builder: Notification) {
    NotificationManagerCompat.from(context)
      .notify(id, builder)
  }
}
