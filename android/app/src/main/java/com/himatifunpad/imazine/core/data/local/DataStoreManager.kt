package com.himatifunpad.imazine.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.himatifunpad.imazine.core.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
  private val prefsDataStore = appContext.dataStore

  suspend fun setUser(user: User) {
    prefsDataStore.edit { prefs ->
      prefs[stringPreferencesKey(Keys.NAMA)] = user.nama
      prefs[stringPreferencesKey(Keys.NPM)] = user.npm
    }
  }

  suspend fun clearUser() {
    prefsDataStore.edit { prefs ->
      prefs[stringPreferencesKey(Keys.NAMA)] = ""
      prefs[stringPreferencesKey(Keys.NPM)] = ""
    }
  }

  val user: Flow<User> = prefsDataStore.data.map { prefs ->
    User(
      nama = prefs[stringPreferencesKey(Keys.NAMA)] ?: "",
      npm = prefs[stringPreferencesKey(Keys.NPM)] ?: ""
    )
  }

  suspend fun setLatestPostId(postId: Long) {
    prefsDataStore.edit { prefs ->
      prefs[longPreferencesKey(Keys.LATEST_POST_ID)] = postId
    }
  }

  val latestPostId: Flow<Long> =
    prefsDataStore.data.map { it[longPreferencesKey(Keys.LATEST_POST_ID)] ?: 0L }

  val notifyNewPost: Flow<Boolean> =
    prefsDataStore.data.map { it[booleanPreferencesKey(Keys.NOTIFY_NEW_POST)] ?: true }

  val appTheme: Flow<Int> =
    prefsDataStore.data.map { it[intPreferencesKey(Keys.APP_THEME)] ?: APP_THEME_SYSTEM }

  suspend fun setInt(key: Preferences.Key<Int>, value: Int) {
    prefsDataStore.edit { prefs -> prefs[key] = value }
  }

  fun getInt(key: Preferences.Key<Int>, default: Int = 0): Flow<Int?> =
    prefsDataStore.data.map { it[key] ?: default }
}
