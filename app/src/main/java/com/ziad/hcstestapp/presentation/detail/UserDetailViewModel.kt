package com.ziad.hcstestapp.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.hcstestapp.domain.usecase.GetUserDetailUseCase
import com.ziad.hcstestapp.domain.usecase.ObserveFavoriteStatusUseCase
import com.ziad.hcstestapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoriteStatusUseCase: ObserveFavoriteStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadUserDetail(username: String) {
        viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading

            launch {
                observeFavoriteStatusUseCase(username).collect { isFavorite ->
                    Log.d("FavoriteDebug", "isFavorite for $username: $isFavorite")
                    _isFavorite.value = isFavorite
                }
            }

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


    fun toggleFavorite(username: String) {
        viewModelScope.launch {
            val newState = !_isFavorite.value
            toggleFavoriteUseCase(username, newState)
        }
    }

    fun retry(username: String) {
        loadUserDetail(username)
    }
}