package com.himatifunpad.imazine.core.data.remote.api

import com.himatifunpad.imazine.core.data.remote.json.AuthJson
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HdaApiService {
  @FormUrlEncoded
  @POST("user/login")
  suspend fun login(
    @Field("username") username: String,
    @Field("password") password: String
  ): Response<AuthJson>
}
