package com.ziad.hcstestapp.presentation.detail

import com.ziad.hcstestapp.domain.model.GithubUser

sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class Success(val user: GithubUser) : UserDetailUiState()
    data class Error(val message: String) : UserDetailUiState()
}