package com.himatifunpad.imazine.core.domain.repository

import com.himatifunpad.imazine.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  suspend fun login(username: String, password: String): Flow<Result<User>>
  fun getUser(): Flow<User>
}
