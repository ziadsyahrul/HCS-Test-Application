package com.ziad.hcstestapp.data.remote.api

import com.ziad.hcstestapp.data.remote.dto.UserDto
import com.ziad.hcstestapp.data.remote.dto.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30
    ): UserSearchResponse

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDto
}