package com.ziad.hcstestapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "bio")
    val bio: String?,

    @ColumnInfo(name = "company")
    val company: String?,

    @ColumnInfo(name = "location")
    val location: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "blog")
    val blog: String?,

    @ColumnInfo(name = "public_repos")
    val publicRepos: Int,

    @ColumnInfo(name = "followers")
    val followers: Int,

    @ColumnInfo(name = "following")
    val following: Int,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "html_url")
    val htmlUrl: String,

    @ColumnInfo(name = "cached_at")
    val cachedAt: Long = System.currentTimeMillis()
)