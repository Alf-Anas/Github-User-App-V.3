package com.lofrus.githubuserfavoriteapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(base_url: String) {
    val instanceListUsers: GitHubGetListUsers by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetListUsers::class.java)
    }

    val instanceDetailUser: GitHubGetDetailUser by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetDetailUser::class.java)
    }

    val instanceFollowers: GitHubGetFollowers by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetFollowers::class.java)
    }

    val instanceFollowing: GitHubGetFollowing by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetFollowing::class.java)
    }

    val instanceRepositories: GitHubGetRepositories by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetRepositories::class.java)
    }
}