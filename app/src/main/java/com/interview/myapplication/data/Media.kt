package com.interview.myapplication.data

import com.google.gson.annotations.SerializedName

data class Media(
    @SerializedName("m")
    val thumbnailUrl: String
)
