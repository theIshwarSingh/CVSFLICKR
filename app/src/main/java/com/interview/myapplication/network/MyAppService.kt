package com.interview.myapplication.network

import com.google.gson.GsonBuilder
import com.interview.myapplication.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyAppService {

    private val gson = GsonBuilder().setLenient().create()

    fun <T> instance(api: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(api)
    }
}