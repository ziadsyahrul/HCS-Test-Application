package com.ziad.hcstestapp.domain.repository

import com.ziad.hcstestapp.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    suspend fun searchUser(query: String): Flow<Result<List<GithubUser>>>
    suspend fun getUserDetail(username: String): Flow<Result<GithubUser>>
    fun getCachedUsers(): Flow<List<GithubUser>>

}