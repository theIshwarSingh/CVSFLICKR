package com.interview.myapplication.network.api

import com.interview.myapplication.BuildConfig
import com.interview.myapplication.data.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RecentPostService {

    @GET(BuildConfig.PHOTO_END_POINT)
    suspend fun getRecentPosts(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("tag") tag: String,
    ): Response<PostResponse>

}