package com.ziad.hcstestapp.domain.usecase

import com.ziad.hcstestapp.domain.repository.GitHubRepository
import com.ziad.hcstestapp.presentation.search.SearchUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: GitHubRepository
) {
    operator fun invoke(query: String): Flow<SearchUiState> = flow {

        if (query.isBlank() || query.length < 3) {
            emit(SearchUiState.Initial)
            return@flow
        }

        emit(SearchUiState.Loading)

        repository.searchUser(query).collect { result ->
            result.fold(
                onSuccess = { users->
                    if (users.isEmpty()) {
                        emit(SearchUiState.Error("No Users Found"))
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