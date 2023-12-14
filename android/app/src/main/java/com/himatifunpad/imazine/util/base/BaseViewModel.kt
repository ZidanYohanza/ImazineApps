package com.himatifunpad.imazine.util.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himatifunpad.imazine.util.base.BaseEvent.ShowErrorMessage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel : ViewModel() {
  private val eventChannel = Channel<BaseEvent>(Channel.BUFFERED)
  val events = eventChannel.receiveAsFlow()

  protected suspend fun sendNewEvent(event: BaseEvent) {
    eventChannel.send(event)
  }

  protected suspend fun setErrorMessage(message: String?) {
    eventChannel.send(ShowErrorMessage(message ?: ""))
  }

  override fun onCleared() {
    super.onCleared()
    viewModelScope.cancel()
  }
}
