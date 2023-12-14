package com.himatifunpad.imazine.core.di

import com.himatifunpad.imazine.core.data.AuthRepositoryImpl
import com.himatifunpad.imazine.core.data.PostRepositoryImpl
import com.himatifunpad.imazine.core.domain.repository.AuthRepository
import com.himatifunpad.imazine.core.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
  /**
   * @param repo
   * @return
   */
  @Binds
  fun authRepository(repo: AuthRepositoryImpl): AuthRepository

  /**
   * @param repo
   * @return
   */
  @Binds
  fun postRepository(repo: PostRepositoryImpl): PostRepository
}
