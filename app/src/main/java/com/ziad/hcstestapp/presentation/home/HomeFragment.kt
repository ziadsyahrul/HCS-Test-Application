package com.ziad.hcstestapp.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziad.hcstestapp.R
import com.ziad.hcstestapp.databinding.ActivityMainBinding
import com.ziad.hcstestapp.databinding.FragmentHomeBinding
import com.ziad.hcstestapp.domain.model.GithubUser
import com.ziad.hcstestapp.presentation.detail.UserDetailActivity
import com.ziad.hcstestapp.presentation.home.adapter.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchInput()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user ->
            val intent = Intent(requireContext(), UserDetailActivity::class.java).apply {
                putExtra(UserDetailActivity.EXTRA_USERNAME, user.login)
            }
            startActivity(intent)
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }
    }

    private fun handleUiState(state: SearchUiState) {
        when (state) {
            is SearchUiState.Initial -> showInitialState()
            is SearchUiState.Loading -> showLoadingState()
            is SearchUiState.Success -> showSuccessState(state.users)
            is SearchUiState.Error -> showErrorState(state.message)
        }
    }

    private fun showInitialState() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}