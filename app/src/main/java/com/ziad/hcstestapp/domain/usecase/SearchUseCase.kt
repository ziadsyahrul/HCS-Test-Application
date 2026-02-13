package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    suspend operator fun invoke(query: String): Flow<Result<List<GithubUser>>> {
        return repository.searchUser(query)
    }
}