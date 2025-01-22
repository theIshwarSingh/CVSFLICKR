package com.interview.myapplication.network.repo

import com.interview.myapplication.network.ResponseStatus
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface RecentPostRepo {

    suspend fun getRecentPosts(tag: String): Flow<ResponseStatus>

}