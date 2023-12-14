package com.himatifunpad.imazine.ui.screen.auth

import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.himatifunpad.imazine.core.data.local.DataStoreManager
import com.himatifunpad.imazine.core.domain.repository.AuthRepository
import com.himatifunpad.imazine.ui.screen.auth.AuthViewModel.AuthEvent.LoginSuccess
import com.himatifunpad.imazine.util.base.BaseEvent
import com.himatifunpad.imazine.util.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val auth: AuthRepository
) : BaseViewModel() {
  @Inject lateinit var prefs: DataStoreManager

  sealed class AuthEvent : BaseEvent() {
    object LoginSuccess : AuthEvent()
  }

  fun doLogin(username: String, password: String) =
    viewModelScope.launch {
      if (username.isEmpty() || password.isEmpty()) {
        setErrorMessage("NPM / password tidak boleh kosong")
        return@launch
      }
      auth.login(username, password)
        .catch {
          setErrorMessage(it.message)
        }
        .collect { result ->
          if (result.isSuccess) {
            sendNewEvent(LoginSuccess)
            if (isPostNotificationOn()) {
              Firebase.messaging.subscribeToTopic("newpost")
            }
          } else if (result.isFailure) setErrorMessage(result.exceptionOrNull()?.message)
        }
    }

  private suspend fun isPostNotificationOn(): Boolean = prefs.notifyNewPost.first()
}
