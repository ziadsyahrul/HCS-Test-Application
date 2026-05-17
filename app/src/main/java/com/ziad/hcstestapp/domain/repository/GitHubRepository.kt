package com.ziad.hcstestapp.domain.repository

import com.ziad.hcstestapp.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    fun getUsers(): Flow<Result<List<GithubUser>>>
    fun searchUser(query: String): Flow<Result<List<GithubUser>>>
    suspend fun getUserDetail(username: String): Flow<Result<GithubUser>>
    fun getCachedUsers(): Flow<List<GithubUser>>
    fun getFavoriteUsers(): Flow<List<GithubUser>>
    suspend fun toggleFavorite(username: String, isFavorite: Boolean)
    fun observeFavoriteStatus(username: String): Flow<Boolean>

}