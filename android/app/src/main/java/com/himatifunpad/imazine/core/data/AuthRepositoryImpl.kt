package com.himatifunpad.imazine.core.data

import com.himatifunpad.imazine.core.data.local.DataStoreManager
import com.himatifunpad.imazine.core.data.remote.SafeApiRequest
import com.himatifunpad.imazine.core.data.remote.api.HdaApiService
import com.himatifunpad.imazine.core.data.remote.json.AuthJson
import com.himatifunpad.imazine.core.domain.model.User
import com.himatifunpad.imazine.core.domain.repository.AuthRepository
import com.himatifunpad.imazine.util.ApiException
import com.himatifunpad.imazine.util.NoInternetException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val api: HdaApiService,
  private val prefs: DataStoreManager
) : AuthRepository, SafeApiRequest() {
  override suspend fun login(username: String, password: String): Flow<Result<User>> = flow {
    try {
      val user = apiRequest(
        { api.login(username, password) },
        ::decodeAuthJson
      ).user ?: throw ApiException("User data is empty")
      val result = user.toModel()
      saveUser(result)
      emit(Result.success(result))
    } catch (ex: ApiException) {
      emit(Result.failure(ex))
    } catch (ex: NoInternetException) {
      emit(Result.failure(ex))
    }
  }

  private fun decodeAuthJson(str: String): String =
    Json.decodeFromString(AuthJson.serializer(), str).status

  private suspend fun saveUser(user: User) {
    prefs.setUser(user)
  }

  override fun getUser(): Flow<User> = prefs.user
}
