package com.himatifunpad.imazine.core.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.notification.NotificationUtil.NotificationData
import com.himatifunpad.imazine.ui.MainActivity
import logcat.logcat

class FCMService : FirebaseMessagingService() {
  companion object {
    const val NEW_POST_NOTIF_ID = 256
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    logcat(tag = "FCM") { "New token is $token" }
  }

  override fun onMessageReceived(message: RemoteMessage) {
    logcat(tag = "FCM") { "From: ${message.from}" }

    if (message.data.isNotEmpty()) {
      logcat(tag = "FCM") { "Payload: ${message.data}" }
    }

    message.notification?.let { notification ->
      logcat(tag = "FCM") { "Notification Body: $notification" }
      NotificationData(
        id = R.string.new_post_notif_channel_id,
        title = notification.title ?: "New article posted!",
        message = notification.body ?: "Check this new article on Imazine!!",
        channel = NotificationUtil.ChannelName.NEW_POST
      ).apply {
        NotificationUtil.showNotification(
          applicationContext,
          NEW_POST_NOTIF_ID,
          buildNewPostNotification(applicationContext, this)
        )
      }
    }
  }

  private fun buildNewPostNotification(
    context: Context,
    data: NotificationData
  ): Notification =
    NotificationCompat.Builder(context, context.getString(data.id))
      .apply {
        val intent = Intent(context, MainActivity::class.java).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
          PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        color = getColor(R.color.amber_500)
        setContentIntent(pendingIntent)
        setSmallIcon(R.drawable.ic_logo_notif)
        setContentTitle(data.title)
        setContentText(data.message)
        setOngoing(false)
        setAutoCancel(true)
      }
      .build()
}
