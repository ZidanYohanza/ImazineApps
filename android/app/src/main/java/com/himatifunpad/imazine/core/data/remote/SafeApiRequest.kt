package com.himatifunpad.imazine.core.data.remote

import com.himatifunpad.imazine.util.ApiException
import org.json.JSONException
import retrofit2.Response

open class SafeApiRequest {
  suspend fun <T : Any> apiRequest(
    call: suspend () -> Response<T>,
    decodeJson: suspend (String) -> String
  ): T {
    val response = call.invoke()
    if (response.isSuccessful) {
      return response.body()!!
    } else {
      val error = response.errorBody()?.string()
      val message = StringBuilder()
      error?.let {
        try {
          message.append(decodeJson(it))
        } catch (e: JSONException) {
          e.printStackTrace()
        }
      }
      throw ApiException(message.toString())
    }
  }
}
