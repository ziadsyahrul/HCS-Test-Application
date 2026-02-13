package com.ziad.hcstestapp.data.repository

import com.ziad.hcstestapp.data.local.dao.UserDao
import com.ziad.hcstestapp.data.mapper.toDomain
import com.ziad.hcstestapp.data.mapper.toEntity
import com.ziad.hcstestapp.data.remote.api.GitHubApiService
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val apiService: GitHubApiService,
    private val userDao: UserDao
) : GitHubRepository {

    override suspend fun searchUser(query: String): Flow<Result<List<GithubUser>>> = flow {
        try {
            val response = apiService.searchUsers(query)
            val users = response.items.map { it.toDomain() }

            val entities = response.items.map { it.toEntity() }
            userDao.insertUsers(entities)

            emit(Result.success(users))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getUserDetail(username: String): Flow<Result<GithubUser>> = flow {
        try {
            val cachedUser = userDao.getUserByUsername(username)
            if (cachedUser != null) {
                emit(Result.success(cachedUser.toDomain()))
            }

            val userDto = apiService.getUserDetail(username)
            val user = userDto.toDomain()

            userDao.insertUser(userDto.toEntity())

            emit(Result.success(user))
        } catch (e: Exception) {
            val cachedUser = userDao.getUserByUsername(username)
            if (cachedUser != null) {
                emit(Result.success(cachedUser.toDomain()))
            } else {
                emit(Result.failure(e))
            }
        }
    }


    override fun getCachedUsers(): Flow<List<GithubUser>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}