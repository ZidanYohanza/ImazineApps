package com.himatifunpad.imazine.core.di.entrypoint

import coil.ImageLoader
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImageLoaderEntryPoint {
  /**
   * @return
   */
  fun coilLoader(): ImageLoader
}
