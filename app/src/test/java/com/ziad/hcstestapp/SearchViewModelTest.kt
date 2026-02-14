package com.ziad.hcstestapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.usecase.GetCachedUsersUseCase
import com.ziad.hcstestapp.domain.usecase.SearchUseCase
import com.ziad.hcstestapp.presentation.search.SearchUiState
import com.ziad.hcstestapp.presentation.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchUsersUseCase: SearchUseCase
    private lateinit var getCachedUsersUseCase: GetCachedUsersUseCase
    private var testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        searchUsersUseCase = mockk()
        getCachedUsersUseCase = mockk()

        coEvery { getCachedUsersUseCase() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Initial`() {
        viewModel = SearchViewModel(searchUsersUseCase, getCachedUsersUseCase)

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Initial)
    }

    @Test
    fun `search with valid query should return success state`() = runTest {
        val query = "john"
        val mockUsers = listOf(
            GithubUser(
                id = 1,
                login = "johndoe",
                avatarUrl = "https://avatar.url",
                name = "John Doe",
                bio = "Developer",
                company = "Tech Corp",
                location = "Jakarta",
                email = "john@example.com",
                blog = "johndoe.dev",
                publicRepos = 50,
                followers = 100,
                following = 80,
                type = "User",
                htmlUrl = "https://github.com/johndoe"
            )
        )

        coEvery { searchUsersUseCase(query) } returns flowOf(Result.success(mockUsers))

        viewModel = SearchViewModel(searchUsersUseCase, getCachedUsersUseCase)
        viewModel.onSearchQueryChanged(query)

        advanceTimeBy(600)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Success)
        assertEquals(mockUsers, (state as SearchUiState.Success).users)
    }

    @Test
    fun `search with error should return error state`() = runTest {
        val query = "john"
        val errorMessage = "Network error"

        coEvery { searchUsersUseCase(query) } returns flowOf(
            Result.failure(Exception(errorMessage))
        )

        viewModel = SearchViewModel(searchUsersUseCase, getCachedUsersUseCase)
        viewModel.onSearchQueryChanged(query)

        advanceTimeBy(600)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Error)
        assertEquals(errorMessage, (state as SearchUiState.Error).message)
    }

    @Test
    fun `search with empty query should return initial state`() {
        viewModel = SearchViewModel(searchUsersUseCase, getCachedUsersUseCase)

        viewModel.onSearchQueryChanged("")

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Initial)
    }

    @Test
    fun `search with short query should not trigger search`() = runTest {
        val query = "j"
        viewModel = SearchViewModel(searchUsersUseCase, getCachedUsersUseCase)

        viewModel.onSearchQueryChanged(query)
        advanceTimeBy(600)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Initial || state is SearchUiState.Loading)
    }

}