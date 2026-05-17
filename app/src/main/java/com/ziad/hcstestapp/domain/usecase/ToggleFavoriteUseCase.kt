package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.repository.GitHubRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: GitHubRepository

) {

    suspend operator fun invoke(username: String, isFavorite: Boolean) {
        repository.toggleFavorite(username, isFavorite)
    }
}