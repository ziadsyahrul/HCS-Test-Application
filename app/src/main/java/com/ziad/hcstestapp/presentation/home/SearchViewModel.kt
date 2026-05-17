package com.ziad.hcstestapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.usecase.GetCachedUsersUseCase
import com.ziad.hcstestapp.domain.usecase.GetUsersUseCase
import com.ziad.hcstestapp.domain.usecase.SearchUseCase
import com.ziad.hcstestapp.presentation.home.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUseCase,
    private val getCachedUsersUseCase: GetCachedUsersUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")


    val uiState: StateFlow<SearchUiState> =
        searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .flatMapMerge { query ->
                if (query.isBlank()) {
                    getUsersUseCase()
                } else {
                    searchUsersUseCase(query)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SearchUiState.Loading
            )

    private val _cachedUsers = MutableLiveData<List<GithubUser>>()
    val cachedUsers: LiveData<List<GithubUser>> = _cachedUsers

    private fun loadCachedUsers() {
        viewModelScope.launch {
            getCachedUsersUseCase().collect { users ->
                _cachedUsers.value = users
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun retry() {
        val query = searchQuery.value
        searchQuery.value = query
    }
}