package com.himatifunpad.imazine.core.di.entrypoint

import com.himatifunpad.imazine.core.data.local.DataStoreManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PrefsEntryPoint {
  /**
   * @return
   */
  fun prefs(): DataStoreManager
}
