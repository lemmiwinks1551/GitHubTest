package com.example.testapp.domain

import com.example.testapp.models.DirContent
import com.example.testapp.models.RepoContent
import com.example.testapp.models.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") query: String
    ): SearchResult.SearchResponse<SearchResult.User>

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): SearchResult.SearchResponse<SearchResult.Repository>

    @GET("repos/{owner}/{repo}/contents")
    suspend fun getRepositoryContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): List<RepoContent>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getDirContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): List<DirContent>
}


