package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.repository.GitHubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteStatusUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(username: String): Flow<Boolean> {
        return repository.observeFavoriteStatus(username)
    }
}