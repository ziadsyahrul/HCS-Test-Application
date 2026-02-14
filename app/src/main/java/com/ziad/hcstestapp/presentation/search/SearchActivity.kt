package com.ziad.hcstestapp.presentation.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziad.hcstestapp.R
import com.ziad.hcstestapp.databinding.ActivityMainBinding
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.presentation.detail.UserDetailActivity
import com.ziad.hcstestapp.presentation.search.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        askNotificationPermission()
        setupToolbar()
        setupRecyclerview()
        setupSearchInput()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupRecyclerview() {
        userAdapter = UserAdapter { user->
            navigateToUserDetail(user)
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.onSearchQueryChanged(query)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }

        viewModel.cachedUsers.observe(this) { users ->
//
        }
    }

    private fun handleUiState(state: SearchUiState) {
        when(state) {
            is SearchUiState.Initial -> {
                showEmptyState()
            }
            is SearchUiState.Loading -> {
                showLoadingState()
            }
            is SearchUiState.Success -> {
                showSuccessState(state.users)
            }
            is SearchUiState.Error -> {
                showErrorState(state.message)
            }
        }
    }

    private fun showEmptyState() {
        binding.apply {
            searchProgress.isVisible = false
            emptyStateLayout.isVisible = true
            loadingLayout.isVisible = false
            errorLayout.isVisible = false
            rvUsers.isVisible = false
        }
    }

    private fun showLoadingState() {
        binding.apply {
            searchProgress.isVisible = true
            emptyStateLayout.isVisible = false
            loadingLayout.isVisible = true
            errorLayout.isVisible = false
            rvUsers.isVisible = false
        }
    }

    private fun showSuccessState(users: List<GithubUser>) {
        binding.apply {
            searchProgress.isVisible = false
            emptyStateLayout.isVisible = false
            loadingLayout.isVisible = false
            errorLayout.isVisible = false
            rvUsers.isVisible = true
        }
        userAdapter.submitList(users)
    }

    private fun showErrorState(message: String) {
        binding.apply {
            searchProgress.isVisible = false
            emptyStateLayout.isVisible = false
            loadingLayout.isVisible = false
            errorLayout.isVisible = true
            rvUsers.isVisible = false
            tvError.text = message
        }
    }

    private fun navigateToUserDetail(user: GithubUser) {
        val intent = Intent(this, UserDetailActivity::class.java).apply {
            Log.d("TAG", "navigateToUserDetail: " + user.login)
            putExtra(UserDetailActivity.EXTRA_USERNAME, user.login)
        }
        startActivity(intent)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
}