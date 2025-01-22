package com.interview.myapplication.network

import com.interview.myapplication.data.Posts

sealed class ResponseStatus {
    object Pending : ResponseStatus()
    object Fetching : ResponseStatus()
    data class FetchedSuccessfully(val thumbnail : List<Posts>) : ResponseStatus()
    data class ErrorOccurred(val message : String) : ResponseStatus()
}
