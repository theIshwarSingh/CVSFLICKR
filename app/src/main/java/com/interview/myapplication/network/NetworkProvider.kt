package com.interview.myapplication.network

import com.interview.myapplication.network.api.RecentPostService
import com.interview.myapplication.network.repo.RecentPostRepo
import com.interview.myapplication.network.repo.RecentRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {

    @Singleton
    @Provides
    fun getRecentPostsService(): RecentPostService = MyAppService.instance(RecentPostService::class.java)

    @Singleton
    @Provides
    fun getRecentPostsRepo(recentPostService: RecentPostService): RecentPostRepo = RecentRepoImpl(recentPostService)
}