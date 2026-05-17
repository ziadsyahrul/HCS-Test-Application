package com.ziad.hcstestapp.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.usecase.GetFavoriteUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteUsersUseCase: GetFavoriteUsersUseCase
) : ViewModel() {

    val favorites: StateFlow<List<GithubUser>> = getFavoriteUsersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}