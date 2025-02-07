package com.example.testapp.domain

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.github.com"

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .header("User-Agent", "GitHubSearchApp")
                .build()

            val response = chain.proceed(request)

            Log.d("Retrofit", "Request: ${request.url}")
            Log.d("Retrofit", "Response Code: ${response.code}")
            response
        }
        .build()

    val api: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GitHubApiService::class.java)
    }
}


