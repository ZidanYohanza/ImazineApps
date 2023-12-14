package com.himatifunpad.imazine

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.himatifunpad.imazine.core.notification.NotificationUtil
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE

@HiltAndroidApp
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    // init component
    AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)
    // notification channel
    NotificationUtil.createNotificationChannel(
      context = this,
      NotificationUtil.NotificationChannelData(
        id = R.string.new_post_notif_channel_id,
        name = NotificationUtil.ChannelName.NEW_POST,
        description = "Notification when a new article is posted",
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
        showBadge = true
      )
    )
  }
}
