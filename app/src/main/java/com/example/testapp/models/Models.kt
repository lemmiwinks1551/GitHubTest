package com.example.testapp.models

import com.google.gson.annotations.SerializedName

sealed class SearchResult {

    data class User(
        /* поля, какие нам нужны из json

        {"login": "lemmiwinks1551",
        "avatar_url": "https://avatars.githubusercontent.com/u/94862109?v=4",
        "html_url": "https://github.com/lemmiwinks1551"}*/

        val login: String,

        @SerializedName("avatar_url")
        val avatarUrl: String,

        @SerializedName("html_url")
        val profileUrl: String
    ) : SearchResult()

    data class Repository(
        /* поля, какие нам нужны из json:

        {"name": "MySchedule",
        "stargazers_count": 2,
        "watchers_count": 2,
        "forks_count": 0,
        "owner": {
                "login": "lemmiwinks1551", ... User()}
         "created_at": "2022-06-14T19:07:28Z",
         "updated_at": "2025-01-24T08:06:44Z",
         "description": null,
         "html_url": "https://github.com/lemmiwinks1551/MySchedule"*/

        val name: String,

        @SerializedName("stargazers_count")
        val stars: Int,

        @SerializedName("watchers_count")
        val watchers: Int,

        @SerializedName("forks_count")
        val forks: Int,

        val owner: User,

        @SerializedName("created_at")
        val createdAt: String,

        @SerializedName("updated_at")
        val updatedAt: String,

        val description: String?,

        @SerializedName("html_url")
        val repoUrl: String
    ) : SearchResult()

    data class SearchResponse<T>(
        val items: List<T>
    )
}

data class RepoContent(
    // "name": ".gitignore"
    // "type": "file"
    // "html_url": "https://github.com/lemmiwinks1551/MySchedule/blob/main/.gitignore",
    // "size": 338
    val name: String,
    val type: String,
    @SerializedName("html_url") val htmlUrl: String,
    val size: String
)

data class DirContent(
    // todo Доделать
    val name: String, // "name": "1.png"
    val type: String, // "type": "file" или "type": "dir"
    val url: String? = null, // "url": "https://api.github.com/repos/lemmiwinks15/..."
    val size: Int? = null, // "size": 599160
)

