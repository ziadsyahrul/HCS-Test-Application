package com.ziad.hcstestapp.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.usecase.GetCachedUsersUseCase
import com.ziad.hcstestapp.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUseCase,
    private val getCachedUsersUseCase: GetCachedUsersUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>(SearchUiState.Initial)
    val uiState: LiveData<SearchUiState> = _uiState

    private val _cachedUsers = MutableLiveData<List<GithubUser>>()
    val cachedUsers: LiveData<List<GithubUser>> = _cachedUsers

    private val searchQuery = MutableStateFlow("")

    init {
        loadCachedUsers()
        observeSearchQuery()
    }

    private fun loadCachedUsers() {
        viewModelScope.launch {
            getCachedUsersUseCase().collect { users ->
                _cachedUsers.value = users
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank() && query.length >= 2) {
                        performSearch(query)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            searchUsersUseCase(query).collect { result ->
                result.fold(
                    onSuccess = { users ->
                        if (users.isEmpty()) {
                            _uiState.value = SearchUiState.Error("No users found")
                        } else {
                            _uiState.value = SearchUiState.Success(users)
                        }
                    },
                    onFailure = { error ->
                        _uiState.value = SearchUiState.Error(
                            error.message ?: "An error occurred"
                        )
                    }
                )
            }
        }
    }

    fun retry() {
        val query = searchQuery.value
        if (query.isNotBlank()) {
            performSearch(query)
        }
    }
}