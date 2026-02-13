package com.ziad.hcstestapp.presentation.search

import com.ziad.hcstestapp.domain.model.GithubUser

sealed class SearchUiState {
    object Initial : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val users: List<GithubUser>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()

}