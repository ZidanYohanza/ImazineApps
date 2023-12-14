package com.himatifunpad.imazine.ui

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap
import android.view.View
import androidx.viewbinding.ViewBinding
import logcat.logcat
import java.lang.reflect.Method

// from https://medium.com/@hoc081098/viewbinding-delegate-one-line-4d0cdcbf53ba
internal object MainHandler {
  private val handler = Handler(Looper.getMainLooper())

  internal fun post(action: () -> Unit): Boolean = handler.post(action)
}

@PublishedApi
internal fun ensureMainThread() = check(Looper.getMainLooper() == Looper.myLooper()) {
  "Expected to be called on the main thread but was " + Thread.currentThread().name
}

internal object GetBindMethod {
  init {
    ensureMainThread()
  }

  private val methodSignature = View::class.java
  private val methodMap = ArrayMap<Class<out ViewBinding>, Method>()

  internal operator fun <T : ViewBinding> invoke(clazz: Class<T>): Method =
    methodMap
      .getOrPut(clazz) { clazz.getMethod("bind", methodSignature) }
      .also { logcat { "methodMap.size: ${methodMap.size}" } }
}
