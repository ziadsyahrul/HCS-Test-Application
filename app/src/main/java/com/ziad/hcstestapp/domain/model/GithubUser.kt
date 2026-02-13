package com.ziad.hcstestapp.domain.model

data class GithubUser(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val blog: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val type: String,
    val htmlUrl: String
)