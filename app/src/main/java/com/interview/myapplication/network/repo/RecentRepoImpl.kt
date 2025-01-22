package com.interview.myapplication.network.repo

import android.util.Log
import com.interview.myapplication.mapper.PostMappers
import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.network.api.RecentPostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject


class RecentRepoImpl @Inject constructor(private val recentPostService: RecentPostService) :
    RecentPostRepo {

    private val TAG = RecentRepoImpl::class.java.simpleName

    override suspend fun getRecentPosts(tag: String) = flow {

        if (tag.isEmpty()) {
            emit(ResponseStatus.Pending)
            Log.e(TAG, "getRecentPosts: Tag is Empty")
            return@flow
        }
        emit(ResponseStatus.Fetching)

        try {
            val response = recentPostService.getRecentPosts(tag = tag)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {

                    val mappedPosts = withContext(Dispatchers.Default) {
                        responseBody.items.map { PostMappers.mapToDomain(it) }
                    }
                    if (mappedPosts.isEmpty()) {
                        emit(ResponseStatus.ErrorOccurred(NO_DATA_FOR_TAG + tag))
                    } else {
                        emit(ResponseStatus.FetchedSuccessfully(mappedPosts))

                    }
                }
            } else if (!response.isSuccessful) {
                emit(ResponseStatus.ErrorOccurred(GENERIC_ERROR))
            }
        }
        catch (e: UnknownHostException) {
            emit(ResponseStatus.ErrorOccurred(CONNECTION_ERROR))
            Log.e(TAG, "Network connectivity issue ${e.message}")
        }
        catch (e: Exception) {
            emit(ResponseStatus.ErrorOccurred(CONNECTION_ERROR))
            Log.e(TAG, "Unexpected error occurred while fetching images ${e.message}")
        }

    }.flowOn(Dispatchers.IO)

    companion object {
        private const val NO_DATA_FOR_TAG = "No Data Found For: "
        private const val GENERIC_ERROR = "An error occurred. Please try again later."
        private const val CONNECTION_ERROR = "Unable to connect. Please check your internet connection and try again."
    }


}
