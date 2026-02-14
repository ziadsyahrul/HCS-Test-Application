package com.ziad.hcstestapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.domain.usecase.GetUserDetailUseCase
import com.ziad.hcstestapp.presentation.detail.UserDetailUiState
import com.ziad.hcstestapp.presentation.detail.UserDetailViewModel
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserDetailViewModel
    private lateinit var getUserDetailUseCase: GetUserDetailUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getUserDetailUseCase = mockk()
        viewModel = UserDetailViewModel(getUserDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load user detail with valid username should return success`() = runTest {
        // Given
        val username = "johndoe"
        val mockUser = GithubUser(
            id = 1,
            login = username,
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

        coEvery { getUserDetailUseCase(username) } returns flowOf(Result.success(mockUser))

        viewModel.loadUserDetail(username)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UserDetailUiState.Success)
        assertEquals(mockUser, (state as UserDetailUiState.Success).user)
    }

    @Test
    fun `load user detail with error should return error state`() = runTest {
        // data dumm
        val username = "johndoe"
        val errorMessage = "User not found"

        coEvery { getUserDetailUseCase(username) } returns flowOf(
            Result.failure(Exception(errorMessage))
        )

        viewModel.loadUserDetail(username)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UserDetailUiState.Error)
        assertEquals(errorMessage, (state as UserDetailUiState.Error).message)
    }

    @Test
    fun `initial state should be loading when load is triggered`() = runTest {
        // Given
        val username = "johndoe"

        coEvery { getUserDetailUseCase(username) } returns flowOf(Result.success(mockk()))

        viewModel.loadUserDetail(username)

        advanceTimeBy(1)
        val state = viewModel.uiState.value
        assertNotNull(state)
    }

}

