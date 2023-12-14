package com.himatifunpad.imazine.core.di

import android.content.Context
import com.himatifunpad.imazine.BuildConfig
import com.himatifunpad.imazine.core.data.remote.NoConnectionInterceptor
import com.himatifunpad.imazine.core.data.remote.api.HdaApiService
import com.himatifunpad.imazine.core.data.remote.api.WpApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import logcat.logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
  companion object {
    private const val DEFAULT_TIMEOUT = 5L
  }

  private val wpUrl = "http://himatif.fmipa.unpad.ac.id/wp-json/wp/v2/"
  private val hdaUrl = "https://api.himatif.org/data/v1/"
  private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
  }

  @Singleton
  @Provides
  fun provideNoConnectionInterceptor(
    @ApplicationContext appContext: Context
  ): NoConnectionInterceptor =
    NoConnectionInterceptor(appContext)

  @Singleton
  @Provides
  fun provideHttpClient(netConn: NoConnectionInterceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
      .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
      .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)
      .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MINUTES)

    if (BuildConfig.DEBUG) {
      val logger = HttpLoggingInterceptor { message ->
        logcat("API") { message }
      }.apply {
        level = HttpLoggingInterceptor.Level.BASIC
      }
      builder.addInterceptor(logger)
    }

    builder.addInterceptor(netConn)
    return builder
      .build()
  }

  @ExperimentalSerializationApi
  @Singleton
  @Provides
  fun provideJsonConverterFactory(): Factory {
    return json.asConverterFactory("application/json".toMediaType())
  }

  @Singleton
  @Provides
  fun provideWpApiService(client: OkHttpClient, factory: Factory): WpApiService {
    val retrofit = Retrofit.Builder()
      .baseUrl(wpUrl)
      .client(client)
      .addConverterFactory(factory)
      .build()

    logcat { "Conn timeout" + client.connectTimeoutMillis.toString() }

    return retrofit.create(WpApiService::class.java)
  }

  @Singleton
  @Provides
  fun provideHdaApiService(client: OkHttpClient, factory: Factory): HdaApiService {
    val retrofit = Retrofit.Builder()
      .baseUrl(hdaUrl)
      .client(client)
      .addConverterFactory(factory)
      .build()

    return retrofit.create(HdaApiService::class.java)
  }
}
