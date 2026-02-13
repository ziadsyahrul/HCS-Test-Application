package com.ziad.hcstestapp.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ziad.hcstestapp.R
import com.ziad.hcstestapp.databinding.ActivityUserDetailBinding
import com.ziad.hcstestapp.domain.model.GithubUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()
    private var currentUser: GithubUser? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupClickListener()
        observeViewModel()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            viewModel.loadUserDetail(username)
        } else {
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupClickListener() {
        binding.btnViewOnGithub.setOnClickListener {
            currentUser?.let { user ->
                openGitHubProfile(user.htmlUrl)
            }
        }

        binding.btnRetry.setOnClickListener {
            val username = intent.getStringExtra(EXTRA_USERNAME)
            if (username != null) {
                viewModel.retry(username)
            }
        }

        binding.blogLayout.setOnClickListener {
            currentUser?.blog?.let { blog ->
                if (blog.isNotEmpty()) {
                    openUrl(blog)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            handleUiState(state)
        }
    }

    private fun handleUiState(state: UserDetailUiState) {
        when (state) {
            is UserDetailUiState.Loading -> {
                showLoadingState()
            }

            is UserDetailUiState.Success -> {
                showSuccessState(state.user)
            }

            is UserDetailUiState.Error -> {
                showErrorState(state.message)
            }
        }
    }

    private fun showLoadingState() {
        binding.apply {
            loadingLayout.isVisible = true
            errorLayout.isVisible = false
            scrollView.isVisible = false
        }
    }

    private fun showSuccessState(user: GithubUser) {
        currentUser = user

        binding.apply {
            loadingLayout.isVisible = false
            errorLayout.isVisible = false
            scrollView.isVisible = true
        }

        bindUserData(user)
    }

    private fun showErrorState(message: String) {
        binding.apply {
            loadingLayout.isVisible = false
            errorLayout.isVisible = true
            scrollView.isVisible = false
            tvError.text = message
        }
    }

    private fun bindUserData(user: GithubUser) {
        binding.apply {
            // load gambar
            if (user.avatarUrl.isNotEmpty()) {
                Glide.with(this@UserDetailActivity)
                    .load(user.avatarUrl)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_placeholder_user)
                    .error(R.drawable.ic_placeholder_user)
                    .into(ivAvatar)
                tvInitials.isVisible = false
            } else {

                ivAvatar.isVisible = false
                tvInitials.isVisible = true
                tvInitials.text = getInitials(user.login)
            }

            tvUsername.text = user.login

            if (!user.bio.isNullOrEmpty()) {
                tvBio.text = user.bio
                tvBio.isVisible = true
            } else {
                tvBio.isVisible = false
            }

            tvReposCount.text = formatNumber(user.publicRepos)
            tvFollowersCount.text = formatNumber(user.followers)
            tvFollowingCount.text = formatNumber(user.following)

            if (!user.company.isNullOrEmpty()) {
                tvCompany.text = user.company
                companyLayout.isVisible = true
            } else {
                companyLayout.isVisible = false
            }

            if (!user.location.isNullOrEmpty()) {
                tvLocation.text = user.location
                locationLayout.isVisible = true
            } else {
                locationLayout.isVisible = false
            }

            if (!user.blog.isNullOrEmpty()) {
                tvBlog.text = user.blog
                blogLayout.isVisible = true
            } else {
                blogLayout.isVisible = false
            }

            if (!user.email.isNullOrEmpty()) {
                tvEmail.text = user.email
                emailLayout.isVisible = true
            } else {
                emailLayout.isVisible = false
            }
        }
    }

    private fun getInitials(username: String): String {
        return if (username.length >= 2) {
            username.substring(0, 2).uppercase()
        } else {
            username.uppercase()
        }
    }

    private fun formatNumber(number: Int): String {
        return when {
            number >= 1000000 -> String.format("%.1fM", number / 1000000.0)
            number >= 1000 -> String.format("%.1fK", number / 1000.0)
            else -> number.toString()
        }
    }

    private fun openGitHubProfile(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openUrl(url: String) {
        try {
            var finalUrl = url
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                finalUrl = "https://$url"
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}