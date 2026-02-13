package com.ziad.hcstestapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.hcstestapp.domain.usecase.GetUserDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UserDetailUiState>()
    val uiState: LiveData<UserDetailUiState> = _uiState

    fun loadUserDetail(username: String) {
        viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading

            getUserDetailUseCase(username).collect { result ->
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = UserDetailUiState.Success(user)
                    },
                    onFailure = { error ->
                        _uiState.value = UserDetailUiState.Error(
                            error.message ?: "Failed to load user detail"
                        )
                    }
                )
            }
        }
    }

    fun retry(username: String) {
        loadUserDetail(username)
    }
}