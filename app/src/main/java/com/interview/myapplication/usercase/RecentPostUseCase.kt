package com.interview.myapplication.usercase

import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.network.repo.RecentPostRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecentPostUseCase@Inject constructor(private val repository: RecentPostRepo) {

    suspend fun invoke(tag: String): Flow<ResponseStatus> {
        return repository.getRecentPosts(tag)
    }

}