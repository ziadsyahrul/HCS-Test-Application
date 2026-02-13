package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedUsersUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(): Flow<List<GithubUser>> {
        return repository.getCachedUsers()
    }
}