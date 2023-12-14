package com.himatifunpad.imazine.core.data.remote.api

import com.himatifunpad.imazine.core.data.remote.json.CategoryJson
import com.himatifunpad.imazine.core.data.remote.json.PostJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WpApiService {
  @GET("posts")
  suspend fun getPosts(
    @Query("page") page: Int,
    @Query("per_page") perPage: Int = 10,
    @Query("categories") category: Int? = null,
    @Query("_embed") embed: String = "wp:term,wp:featuredmedia",
    @Query("_fields") fields: String = "id,date,link,title,slug,content,_links"
  ): Response<List<PostJson>>

  @GET("posts")
  suspend fun getLatestPost(
    @Query("page") page: Int = 1,
    @Query("per_page") perPage: Int = 1,
    @Query("categories") category: Int? = null,
    @Query("_embed") embed: String = "wp:term,wp:featuredmedia",
    @Query("_fields") fields: String = "id,date,link,title,slug,content,_links"
  ): Response<List<PostJson>>

  @GET("posts/{id}")
  suspend fun getPost(
    @Path("id") postId: Long,
    @Query("_embed") embed: String = "wp:term,wp:featuredmedia",
    @Query("_fields") fields: String = "id,date,link,title,slug,content,_links"
  ): Response<PostJson>

  @GET("categories")
  suspend fun getCategories(
    @Query("page") page: Int,
    @Query("per_page") perPage: Int = 20,
    @Query("_fields") fields: String = "id,name,slug"
  ): Response<List<CategoryJson>>
}
