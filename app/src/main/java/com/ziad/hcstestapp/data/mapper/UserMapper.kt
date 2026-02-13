package com.ziad.hcstestapp.data.mapper

import com.ziad.hcstestapp.data.local.entity.UserEntity
import com.ziad.hcstestapp.data.remote.dto.UserDto
import com.ziad.hcstestapp.domain.model.GithubUser

fun UserDto.toDomain(): GithubUser {
    return GithubUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        name = name,
        bio = bio,
        company = company,
        location = location,
        email = email,
        blog = blog,
        publicRepos = publicRepos ?: 0,
        followers = followers ?: 0,
        following = following ?: 0,
        type = type,
        htmlUrl = htmlUrl
    )
}

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        name = name,
        bio = bio,
        company = company,
        location = location,
        email = email,
        blog = blog,
        publicRepos = publicRepos ?: 0,
        followers = followers ?: 0,
        following = following ?: 0,
        type = type,
        htmlUrl = htmlUrl
    )
}

fun UserEntity.toDomain(): GithubUser {
    return GithubUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        name = name,
        bio = bio,
        company = company,
        location = location,
        email = email,
        blog = blog,
        publicRepos = publicRepos,
        followers = followers,
        following = following,
        type = type,
        htmlUrl = htmlUrl
    )
}