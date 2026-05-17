package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.repository.GitHubRepository
import com.ziad.hcstestapp.presentation.home.SearchUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(): Flow<SearchUiState> = flow {
        emit(SearchUiState.Loading)

        repository.getUsers().collect { result ->
            result.fold(
                onSuccess = { users ->
                    if (users.isEmpty()) {
                        emit(SearchUiState.Error("No users found"))
                    } else {
                        emit(SearchUiState.Success(users))
                    }
                },
                onFailure = {
                    emit(SearchUiState.Error(it.message ?: "Error"))
                }
            )
        }
    }
}